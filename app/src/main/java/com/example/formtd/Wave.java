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


//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private Paint paint;
    private AssetManager asset;
    private final Handler waveHandler;
    private Runnable waveRunnable;
    public Enemy[] enemy;
    private int enemyAmount;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 80;  //This is the time difference in the handler/timer
    public boolean pathNeedsUpdating = false;   //Set to true upon tower being built. That then will trigger a path update.
    private Point[] centerPoints;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(AssetManager asset, String enemyType, int enemyAmount){
        //This paint is for the shadow
        paint = new Paint();
        paint.setARGB(17, 10, 10, 10);
        waveHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        this.asset = asset;
        //Enemy array
        this.enemyAmount = enemyAmount;
        this.enemy = new Enemy[enemyAmount];
        for (int i = 0; i < this.enemyAmount; i++) {
            this.enemy[i] = createEnemy(enemyType);
            this.enemy[i].y -= (i*enemySpacing);
            this.enemy[i].enemyWayPoints = enemy[i].breadthSearch.getStartToCenterPath(new Point(enemy[i].x, enemy[i].y));
        }
        this.active = false;    //If this wave is active. If not active, it will not be drawn.
        //Centerpoint
        centerPoints = new Point[]{
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth)
        };

    }

    public void startWave(){
        this.active = true;
    }

    public boolean endWave(){
        for (Enemy enemy : enemy) {
            if(enemy.alive)
                return false;
        }
        return true;
    }


    public void drawWave(Canvas canvas){

        //Draw the actual enemies
        for (Enemy enemy: enemy) {
            if(enemy.alive) {
                canvas.drawCircle(enemy.x + DefenceView.tileWidth / 2, enemy.y + DefenceView.tileWidth*2/5, DefenceView.tileWidth / 3, paint);  //shadow
                canvas.drawBitmap(enemy.art, enemy.x - enemy.art.getWidth() / 6, enemy.y - enemy.art.getHeight()*3/5, null);                   //unit
            }
        }


        //Handler that constantly updates enemy positions.
        waveRunnable = new Runnable() {
            public void run() {
                for (Enemy enemy: enemy) {
                    //Constantly update enemy breadthSearch unless enemy hasn't entered the world yet.
                    if(enemy.alive) {
                        if(!enemy.reachedCenter) {
                            enemy.enemyWayPoints = enemy.breadthSearch.getStartToCenterPath(new Point(enemy.x, enemy.y));
                            //If enemy hasn't spawned yet, then let enemy reach spawn spot first
                            if (!(enemy.y < DefenceView.yGridStart)) {
                                enemy.currentWayPoint = 0;  //Reset waypoint to use new waypoints.
                            }
                        }
                        else{
                            enemy.enemyWayPoints = enemy.breadthSearch.getCenterToEndPath(new Point(enemy.x, enemy.y));
                        }
                    }

                    //If enemy health is zero, then mark enemy as dead and check if wave needs to be ended.
                    if(enemy.health <= 0){
                        enemy.alive = false;
                        if(endWave()){
                            active = false;
                        }
                    }

                    //Check for leaking
                    if (enemy.y >= DefenceView.grid[DefenceView.grid.length - 1][0].y) { //If over last waypoint, see if the enemy has leaked.
                        if(enemy.alive) {
                            DefenceView.lives -= 1;
                            enemy.alive = false;
                            if(endWave()){
                                active = false;
                            }
                        }
                    } else if (enemy.currentWayPoint < enemy.enemyWayPoints.size()) {   //Update enemy position
                        if (enemy.y == enemy.enemyWayPoints.get(enemy.currentWayPoint).y && enemy.x == enemy.enemyWayPoints.get(enemy.currentWayPoint).x) { //If enemy has reached waypoint:
                            enemy.currentWayPoint++;     //Increment to next waypoint
                        } else if (enemy.x < enemy.enemyWayPoints.get(enemy.currentWayPoint).x) {
                            enemy.x++;
                        } else if (enemy.x > enemy.enemyWayPoints.get(enemy.currentWayPoint).x) {
                            enemy.x--;
                        } else if (enemy.y < enemy.enemyWayPoints.get(enemy.currentWayPoint).y) {
                            enemy.y++;
                        } else if (enemy.y > enemy.enemyWayPoints.get(enemy.currentWayPoint).y) {
                            enemy.y--;
                        }
                    }

                    //If enemy hasn't reached center, check if it has.
                    if (enemyCenterReached(new Point(enemy.x, enemy.y)) && !enemy.reachedCenter) {
                        enemy.reachedCenter = true;
                    }
                }
            }
        };
        waveHandler.postDelayed(waveRunnable, enemy[0].animDelay); //Start animation
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
            default:
                throw new Resources.NotFoundException();
        }
    }

}
