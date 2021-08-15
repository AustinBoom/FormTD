package com.example.formtd;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.enemies.Enemy;
import com.example.formtd.enemies.GhostEnemy;
import com.example.formtd.enemies.MinerEnemy;
import com.example.formtd.towers.Tower;


//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private Paint paint;
    private AssetManager asset;
    private final Handler waveHandler;
    private Runnable waveRunnable;
    public Enemy[] enemies;
    private int enemyAmount;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 100;  //This is the time difference in the handler/timer
    public boolean pathNeedsUpdating = false;   //Set to true upon tower being built. That then will trigger a path update.
    private Point[] centerPoints;
    private final int tolerance = 5;      //this is how "off" an enemy can be from a point when traversing the path. (Not yet implemented.)
    public int waveID;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(AssetManager asset, String enemyType, int enemyAmount, int waveID){
        //This paint is for the shadow
        paint = new Paint();
        paint.setARGB(17, 10, 10, 10);
        waveHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        this.asset = asset;
        //Enemy array
        this.enemyAmount = enemyAmount;
        this.enemies = new Enemy[enemyAmount];
        for (int i = 0; i < this.enemyAmount; i++) {
            this.enemies[i] = createEnemy(enemyType);
            this.enemies[i].y -= (i*enemySpacing);
            this.enemies[i].enemyWayPoints = enemies[i].breadthSearch.getStartToCenterPath(new Point(enemies[i].x, enemies[i].y));
        }
        this.active = false;    //If this wave is active. If not active, it will not be drawn.
        //Centerpoint
        centerPoints = new Point[]{
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth)
        };
        this.waveID = waveID;
    }

    public void startWave(){
        this.active = true;
    }

    public boolean endWave(){
        for (Enemy enemy : enemies) {
            if(enemy.alive)
                return false;
        }
        return true;
    }


    public synchronized void drawWave(Canvas canvas){

        //Draw the actual enemies
        for (Enemy enemy: enemies) {
            if(enemy.alive) {
                canvas.drawCircle(enemy.x + DefenceView.tileWidth / 2, enemy.y + DefenceView.tileWidth*2/5, DefenceView.tileWidth / 3, paint);  //shadow
                canvas.drawBitmap(enemy.art, enemy.x - enemy.art.getWidth() / 6, enemy.y - enemy.art.getHeight()*3/5, null);                   //unit
            }
        }


        //Handler that constantly updates enemy positions.
        waveRunnable = new Runnable() {
            public void run() {
                /**Enemy Management**/
                for (int i = 0; i < enemies.length; i++) {
                    //Constantly update enemy breadthSearch unless enemy hasn't entered the world yet.
                    if(enemies[i].alive) {
                        if(!enemies[i].reachedCenter) {
                             enemies[i].enemyWayPoints = enemies[i].breadthSearch.getStartToCenterPath(new Point(enemies[i].x, enemies[i].y));
                            //If enemy hasn't spawned yet, then let enemy reach spawn spot first
                            if (!(enemies[i].y < DefenceView.yGridStart)) {
                                enemies[i].currentWayPoint = 0;  //Reset waypoint to use new waypoints.
                            }
                        }
                        else{
                            enemies[i].enemyWayPoints = enemies[i].breadthSearch.getCenterToEndPath(new Point(enemies[i].x, enemies[i].y));
                        }
                    }

                    //If enemy health is zero, then mark enemy as dead and check if wave needs to be ended.
                    if(enemies[i].health <= 0){
                        enemies[i].kill();
                        if(endWave()){
                            active = false;
                        }
                    }

                    //Check for leaking
                    if (enemies[i].y >= DefenceView.grid[DefenceView.grid.length - 1][0].y) { //If over last waypoint, see if the enemy has leaked.
                        if(enemies[i].alive) {
                            DefenceView.lives -= 1;
                            enemies[i].alive = false;
                            if(endWave()){
                                active = false;
                            }
                        }
                    } else if (enemies[i].currentWayPoint < enemies[i].enemyWayPoints.size()) {   //Update enemy position
                        if (enemies[i].y == enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).y && enemies[i].x == enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).x) { //If enemy has reached waypoint:
                            enemies[i].currentWayPoint++;     //Increment to next waypoint
                        } else if (enemies[i].x < enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).x) {
                            enemies[i].x += enemies[i].movementSpeed;
                        } else if (enemies[i].x > enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).x) {
                            enemies[i].x -= enemies[i].movementSpeed;
                        } else if (enemies[i].y < enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).y) {
                            enemies[i].y += enemies[i].movementSpeed;
                        } else if (enemies[i].y > enemies[i].enemyWayPoints.get(enemies[i].currentWayPoint).y) {
                            enemies[i].y -= enemies[i].movementSpeed;
                        }
                    }

                    //If enemy hasn't reached center, check if it has.
                    if (enemyCenterReached(new Point(enemies[i].x, enemies[i].y)) && !enemies[i].reachedCenter) {
                        enemies[i].reachedCenter = true;
                    }
                }

                /**Tower management**/
                for (Tower tower : DefenceView.towers) {
                    tower.watch(enemies, waveID);
                }
            }
        };
        waveHandler.postDelayed(waveRunnable, 0); //Start animation
    }


    public boolean enemyCenterReached(Point enemyPos){
        if(enemyPos.x >= centerPoints[0].x && enemyPos.x <= centerPoints[1].x && enemyPos.y >= centerPoints[0].y && enemyPos.y <= centerPoints[3].y){
            return true;
        }
        return false;
    }


    public Enemy createEnemy(String enemy){
        switch (enemy){
            case "ghost":
                return new GhostEnemy(asset);
            case "miner":
                return new MinerEnemy(asset);
            default:
                throw new Resources.NotFoundException();
        }
    }

}
