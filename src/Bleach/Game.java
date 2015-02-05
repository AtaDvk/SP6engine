package Bleach;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import Bleach.InputManager.Receptionist;
import Bleach.InputManager.Receptionist.KeyBinding;
import Bleach.PhysicsEngine.Physique;
import Bleach.PhysicsEngine.Physique.CollisionListener;

/*
 * This is for testing the game engine.
 * This is where the game developer resides.
 * 
 * */

public class Game {

	public static void main(String[] args) {

		Bleach myGame = new Bleach();

		myGame.loadImages("assets/images/assets.json");
		myGame.loadSounds("assets/sounds/assets.json");

		myGame.setFPS(60);

		myGame.setSize(800, 600);
		myGame.setTitle("My super game!");

		Level firstLevel = new Level(2800, 1200, "Town");

		firstLevel.addBackground(myGame.getTexture("clouds"));
		firstLevel.addBackground(myGame.getTexture("sky"));
		EntityBlob blobby = new EntityBlob(myGame.getSprite("blob"), 200, 264);
		Player player = new Player(myGame.getSprite("mushi"), 100, 100);
		firstLevel.addMobile(blobby);
		firstLevel.addPlayer(player);

		firstLevel.addTerrainBlock(new TerrainBlock(myGame.getSprite("block"), 1, 14));
		firstLevel.addTerrainBlock(new TerrainBlock(myGame.getSprite("block"), 2, 14));
		firstLevel.addTerrainBlock(new TerrainBlock(myGame.getSprite("block"), 3, 14));
		firstLevel.addTerrainBlock(new TerrainBlock(myGame.getSprite("block"), 4, 14));
		firstLevel.addTerrainBlock(new TerrainBlock(myGame.getSprite("block"), 4, 13));

		// firstLevel.setMusicTrack("melody7");

		myGame.addLevel(firstLevel);

		myGame.init();

		// Adding a hot receptionist
		Receptionist receptionist = new Receptionist() {

			@Override
			public void handleEvent(ActionEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void handleEvent(MouseEvent event) {
				// System.out.println("X: " + event.getX() + " Y: " +
				// event.getY());
			}
		};

		// Telling to receptionist to listen for whenever the LEFT-arrow button
		// is pushed. When it is, blobby's vector-angle is set to 180 degrees.
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("control RIGHT"), "RIGHT", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				blobby.setVectorAngle(Math.toRadians(180));
				System.out.println("CTRL + RIGHT = pushed");
			}
		}));

		// Telling to receptionist to listen for whenever the LEFT-arrow button
		// is pushed. When it is, blobby's vector-angle is set to 180 degrees.
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released RIGHT"), "released", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("RIGHT = released");
			}
		}));
		
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed A"), "pressed A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.setVectorAngle(Math.PI);
				player.isMoving(true);
				System.out.println("Player moving to the left.");
			}
		}));
		
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released A"), "released A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
			}
		}));
		
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed D"), "pressed D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.setVectorAngle(0);
				player.isMoving(true);
				System.out.println("Player moving to the right.");
			}
		}));
		
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released D"), "released D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
			}
		}));


		((Entity) player).setOnCollision(new CollisionListener() {

			@Override
			public void onCollision(Entity collidedWith) {
				System.out.println("Krockade med " + collidedWith.toString());
			}
		
		});
		
		myGame.addReceptionist(receptionist);

		myGame.run();
	}

}
