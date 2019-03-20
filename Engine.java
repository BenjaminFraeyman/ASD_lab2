package org.ibcn.gso.labo2;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.ibcn.gso.labo2.SceneObject.Type;

public class Engine extends AnimationTimer {

    //TODO: set this constant to true once you implemented the Object pool
    private static final boolean OPTIMIZE_SHELL_EXECUTION_TIME = true;
    //TODO: set this constant to true once you implemented the SpriteLLoader
    private static final boolean OPTIMIZE_ANIMATIONS = true;
    private static final int SPEED_RANGE = 30;

    private final int width;
    private final int height;
    private final GraphicsContext gc;
    private final Map<String, SceneObject> objects = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private ObjectPool<CommandRuntime> processorPool = null;
    private volatile long indexCounter = 1;

    public Engine(int width, int height, GraphicsContext gc) throws Exception {
        this.width = width;
        this.height = height;
        this.gc = gc;

        SceneObject obj = new SceneObject();
        obj.setLocation(new double[]{200.0, 200.0});
        obj.setSpeed(new double[]{2.5, 1.5});
        obj.setType(Type.BASKETBALL);

        objects.put("ball1", obj);

        //TODO: init the processorPool here
        processorPool = new CommandRuntimePool(this);
    }

    public Map<String, SceneObject> getObjects() {
        return objects;
    }

    public double getRandomX() {
        return random.nextInt(width - getSize(Type.BASKETBALL));
    }

    public double getRandomY() {
        return random.nextInt(height - getSize(Type.BASKETBALL));
    }

    public String generateId() {
        return "ball" + (++indexCounter);
    }

    //Returns a status message
    public String processInput(String command) {
        try {
            long start = System.currentTimeMillis();
            if (OPTIMIZE_SHELL_EXECUTION_TIME) {
                //TODO write code here to execute the command using the processorPool
                processorPool.getInstance().execute(command);
                
            } else {
                new CommandRuntime(this).execute(command);
            }
            return "Command executed in " + (System.currentTimeMillis() - start) + " ms.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error, could not execute command!";
        }
    }

    public void moveAll() {
        objects.forEach((id, obj)
                -> obj.setSpeed(new double[]{random.nextInt(SPEED_RANGE) - (SPEED_RANGE / 2.0), random.nextInt(SPEED_RANGE) - (SPEED_RANGE / 2.0)})
        );
    }

    @Override
    public void handle(long now) {
        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        //For each object
        objects.forEach((id, obj) -> {
            if (obj.getSpeed() != null && obj.getSpeed().length > 1) {
                //1. Check wall-bounces
                if ((obj.getLocation()[0] + obj.getSpeed()[0]) < 0 || (obj.getLocation()[0] + obj.getSpeed()[0]) > (width - getSize(obj.getType()))) {
                    obj.getSpeed()[0] = -obj.getSpeed()[0];
                }
                if ((obj.getLocation()[1] + obj.getSpeed()[1]) < 0 || (obj.getLocation()[1] + obj.getSpeed()[1]) > (height - getSize(obj.getType()))) {
                    obj.getSpeed()[1] = -obj.getSpeed()[1];
                }

                //2. Update location
                obj.getLocation()[0] += obj.getSpeed()[0];
                obj.getLocation()[1] += obj.getSpeed()[1];
            }

            //3. Draw
            gc.drawImage(getSprite(obj.getType()), obj.getLocation()[0], obj.getLocation()[1]);
            gc.setFill(Color.WHITE);
            gc.fillText(id, obj.getLocation()[0], obj.getLocation()[1]);
        });
    }

    private int getSize(Type type) {
        switch (type) {
            case BASKETBALL:
                return 150;
            case TENNISBALL:
                return 50;
            default:
                return 115;
        }
    }

    private Image getSprite(Type type) {
        if (OPTIMIZE_ANIMATIONS) {
            //TODO: write code here to load sprites using the SpriteLoader
            SpriteLoader spro = SpriteLoader.getinstance();
            return spro.dosomething(type);
        } else {
            switch (type) {
                case BASKETBALL:
                    return new Image(Engine.class.getResourceAsStream("/basketball.png"));
                case TENNISBALL:
                    return new Image(Engine.class.getResourceAsStream("/tennisball.png"));
                default:
                    return new Image(Engine.class.getResourceAsStream("/football.png"));
            }
        }
    }

}
