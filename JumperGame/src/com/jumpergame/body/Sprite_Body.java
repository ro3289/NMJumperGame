package com.jumpergame.body;

import org.andengine.entity.IEntity;

public class Sprite_Body {
    private IEntity mEntity;
    private String mName;
    
    public Sprite_Body(IEntity entity, String name) {
        setEntity(entity);
        setName(name);
    }
    
    public IEntity getEntity() {
        return mEntity;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setEntity(IEntity entity) {
        mEntity = entity;
    }
    
    public void setName(String name) {
        mName = name;
    }
}