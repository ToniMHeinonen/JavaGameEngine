/*package fi.tuni.engine;

public class GameEngine implements Runnable
{
    private Thread thread;
    public boolean running = false;
    private final double UPDATE_CAP = 1.0/60.0;

    public GameEngine() {

    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    public void stop() {

    }

    public void run() {
        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        while(running) {
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;

            while(unprocessedTime >= UPDATE_CAP) {
                unprocessedTime = UPDATE_CAP;
                render = true;
                System.out.println("update");
                //TODO: Update game
            }
        }
        
        if(render) {
            //TODO: REnder game
        } else {
            try {
            Thread.sleep(1);
            } catch(InterruptedException e) {
            e.printStackTrace();
            }
        }
        dispose();
    }

    public void dispose() {

    }

    public static void main(String[] args) {
        GameEngine gc = new GameEngine();
        gc.start();
    }
}
*/