package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
//proofread
public class UniverseList<T extends Group> {
	public UniverseList(ObservableList<Node> objList,
			ArrayList<Universe> universeAvatars) {
		this.objList = objList;
		this.universeAvatars = universeAvatars;
	}

	@SuppressWarnings("unchecked")
	public T get(int i) {
		return (T) objList.get(i);
	}

	public void add(T s) {
		objList.add(s);
		for (Universe avatar : universeAvatars) {
			getList(s, avatar).add( ((AvatarFactory) s).createAvatar());
//			avatar.getChildren().add(((AvatarFactory) s).createAvatar());
		}
	}

	private UniverseList getList(T s, Universe avatar) {
		if (s instanceof Ship) {
			return avatar.getShips();
		} else if (s instanceof Galaxy) {
			return avatar.getGalaxies();
		} else if (s instanceof Bot) {
			return avatar.getBots();
		} else if (s instanceof Missle) {
			return avatar.getMissles();
		} else {
			String msg = "UniverseList.getList(): Unrecognized Type";
			new Exception(msg).printStackTrace();
			System.exit(1);
			return null;
		}
	}

	public int size(){
		return objList.size();
	}
	
	public void remove(int nthObj) {
		T obj = (T) objList.get(nthObj);
		objList.remove(nthObj);
		for (int i = 0; i < universeAvatars.size(); i++) {
			Universe avatar = universeAvatars.get(i);
			getList(obj, avatar).remove(nthObj);
		}
	}

	ObservableList<Node> objList;
	ArrayList<Universe> universeAvatars;
}
