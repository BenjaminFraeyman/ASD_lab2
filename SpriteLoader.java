/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ibcn.gso.labo2;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.ibcn.gso.labo2.SceneObject.Type;

/**
 *
 * @author pollepel
 */
public class SpriteLoader {
    private static SpriteLoader sprite;
    public static Image basketBall;
    public static Image tennisBall;
    public static Image footBall;
    
    private SpriteLoader(){
        basketBall = new Image(Engine.class.getResourceAsStream("/basketball.png"));
        tennisBall = new Image(Engine.class.getResourceAsStream("/tennisball.png"));
        footBall = new Image(Engine.class.getResourceAsStream("/football.png"));
    }
    
    public static SpriteLoader getinstance(){
        if (sprite == null) {
            sprite = new SpriteLoader();
        }
        return sprite;
    }
    
    public Image dosomething (Type type){
        switch (type) {
                case BASKETBALL:
                     return this.basketBall;
                case TENNISBALL:
                    return this.tennisBall;
                default:
                    return this.footBall;
            }
    }
}
