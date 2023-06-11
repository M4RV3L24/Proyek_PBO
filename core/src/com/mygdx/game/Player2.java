package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player2 {
    public Player2() {
        this.generatePlayerAnimation();
    }

    int HP = 100;
    int dmg = 5;

    float stateTime = 0.0f;
    float x, y;
    float dx=0, dy=0, speed=200;
    Direction animationDirection = Direction.LEFT;
    Direction direction = Direction.LEFT;
    State state = State.IDLE;
    Action act = Action.NO_ATTACK;


    enum State {
        IDLE,
        RUN,
        JUMP,
        FALL
    }
    enum Direction {
        LEFT,
        RIGHT
    }

    enum Action {
        ATTACK,
        NO_ATTACK
    }

    Animation<TextureRegion> idleLeftAnimation, runLeftAnimation, idleRightAnimation, runRightAnimation, runRightJump, runLeftJump, runRightAttack, runLeftAttack,
            runRightDeath, runLeftDeath, runRightHitted, runLeftHitted, runRightFall, runLeftFall;

    void minusHP (int dmg) {
        HP-=dmg;
    }


    public void generatePlayerAnimation()
    {
        MyGdxGame app = (MyGdxGame) Gdx.app.getApplicationListener();
        AssetManager assetManager = app.getManager();

        Texture idle = assetManager.get("Sprite2/Idle.png");
        Texture run = assetManager.get("Sprite2/Run.png");
        Texture attack1 = assetManager.get("Sprite2/Attack1.png");
        Texture jump = assetManager.get("Sprite2/Jump.png");
        Texture death = assetManager.get("Sprite2/Death.png");
        Texture hitted = assetManager.get("Sprite2/Take Hit.png");
        Texture fall = assetManager.get("Sprite2/Fall.png");

        //membuat animasi diam hadap kanan
        TextureRegion[] frames = MyGdxGame.CreateAnimationFrames(idle, idle.getWidth()/4, idle.getHeight(), 4, false, false);
        idleRightAnimation = new Animation<TextureRegion>(0.09f, frames);

        //membuat animasi diam hadap kiri, sumbu x di-flip
        frames = MyGdxGame.CreateAnimationFrames(idle, idle.getWidth()/4, idle.getHeight(), 4, true, false);
        idleLeftAnimation = new Animation<TextureRegion>(0.09f, frames);

        //membuat animasi jalan hadap kanan
        frames = MyGdxGame.CreateAnimationFrames(run, run.getWidth()/8, run.getHeight(), 8, false, false);
        runRightAnimation = new Animation<TextureRegion>(0.09f, frames);

        //membuat animasi jalan hadap kiri, sumbu x di-flip
        frames = MyGdxGame.CreateAnimationFrames(run, run.getWidth()/8, run.getHeight(), 8, true, false);
        runLeftAnimation = new Animation<TextureRegion>(0.09f, frames);

        //animasi loncat ke kanan
        frames = MyGdxGame.CreateAnimationFrames(jump, jump.getWidth()/2, jump.getHeight(), 2, false, false);
        runRightJump = new Animation<TextureRegion>(0.09f, frames);

        //animasi loncat ke kiri
        frames = MyGdxGame.CreateAnimationFrames(jump, jump.getWidth()/2, jump.getHeight(), 2, true, false);
        runLeftJump = new Animation<TextureRegion>(0.09f, frames);

        //attack hadap kanan
        frames = MyGdxGame.CreateAnimationFrames(attack1, attack1.getWidth()/4, attack1.getHeight(), 4, false, false);
        runRightAttack = new Animation<TextureRegion>(0.09f, frames);

        //attack hadap kiri
        frames = MyGdxGame.CreateAnimationFrames(attack1, attack1.getWidth()/4, attack1.getHeight(), 4, true, false);
        runLeftAttack = new Animation<TextureRegion>(0.09f, frames);

        //mati hadap kanan
        frames = MyGdxGame.CreateAnimationFrames(death, death.getWidth()/7, death.getHeight(), 7, false, false);
        runRightDeath = new Animation<TextureRegion>(0.09f, frames);

        //mati hadap kiri
        frames = MyGdxGame.CreateAnimationFrames(death, death.getWidth()/7, death.getHeight(), 7, true, false);
        runLeftDeath = new Animation<TextureRegion>(0.09f, frames);

        //kena hit dari kanan
        frames = MyGdxGame.CreateAnimationFrames(hitted, hitted.getWidth()/3, hitted.getHeight(), 3, false, false);
        runRightHitted = new Animation<TextureRegion>(0.09f, frames);


        //kena hit dari kiri
        frames = MyGdxGame.CreateAnimationFrames(hitted, hitted.getWidth()/3, hitted.getHeight(), 3, true, false);
        runLeftHitted= new Animation<TextureRegion>(0.09f, frames);


        frames = MyGdxGame.CreateAnimationFrames(fall, fall.getWidth()/2, fall.getHeight(), 2, false, false);
        runRightFall = new Animation<TextureRegion>(0.09f, frames);

        frames = MyGdxGame.CreateAnimationFrames(fall, fall.getWidth()/2, fall.getHeight(), 2, true, false);
        runLeftFall = new Animation<TextureRegion>(0.09f, frames);
    }

    public void draw(SpriteBatch batch)
    {
        TextureRegion currentFrame = null;
        if(state == State.RUN && animationDirection == Direction.LEFT && act == Action.NO_ATTACK)
            currentFrame = runLeftAnimation.getKeyFrame(stateTime, true);
        else if(state == State.RUN && animationDirection == Direction.RIGHT && act == Action.NO_ATTACK )
            currentFrame = runRightAnimation.getKeyFrame(stateTime, true);
        else if(state == State.IDLE && animationDirection == Direction.LEFT && act == Action.NO_ATTACK)
            currentFrame = idleLeftAnimation.getKeyFrame(stateTime, true);
        else if(state == State.IDLE && animationDirection == Direction.RIGHT && act == Action.NO_ATTACK)
            currentFrame = idleRightAnimation.getKeyFrame(stateTime, true);
        else if ((state == State.IDLE || state == State.RUN) && animationDirection == Direction.RIGHT && act == Action.ATTACK){
            currentFrame = runRightAttack.getKeyFrame(stateTime, true);
        }
        else if ((state == State.IDLE || state == State.RUN) && animationDirection == Direction.LEFT && act == Action.ATTACK){
            currentFrame = runLeftAttack.getKeyFrame(stateTime, true);
        }

        else if ((state == State.JUMP) && animationDirection == Direction.RIGHT) {
            currentFrame = runRightJump.getKeyFrame(stateTime, true);
        }
        else if (state == State.FALL && animationDirection == Direction.RIGHT) {
            currentFrame = runRightFall.getKeyFrame(stateTime, true);
        }
        else if ((state == State.JUMP) && animationDirection == Direction.LEFT) {
            currentFrame = runLeftJump.getKeyFrame(stateTime, true);
        }
        else if (state == State.FALL && animationDirection == Direction.LEFT) {
            currentFrame = runLeftFall.getKeyFrame(stateTime, true);
        }





        batch.draw(currentFrame, x-100, y-100);
    }

    public void update()
    {
        float elapsed = Gdx.graphics.getDeltaTime();
        stateTime += elapsed;

        x += dx * speed * elapsed;
        if(x > MyGdxGame.WORLD_WIDTH-20)
        {
            x = MyGdxGame.WORLD_WIDTH-20;
            this.Stop();
        }
        else if(x < 20)
        {
            x = 20;
            this.Stop();
        }


        y += dy * speed * elapsed;
        if(y > 200+100)
        {
            y = 300;
            dy = -1;
            this.Jump(State.FALL);
        }
        else if(y < 140)
        {
            y = 140;
            this.Stop();
        }
    }

    void Jump (State s){
        if (state == State.IDLE && s == State.JUMP) {
            state = State.JUMP;
            dy = 1;
        }
        if (state == State.JUMP && s == State.FALL) {
            state = State.FALL;
            dy = -1;
        }
    }

    public void setMove(Direction d)
    {
        direction = d;
        state = State.RUN; // update state dan arah animasi

        if(animationDirection == Direction.LEFT && d == Direction.RIGHT)
        {
            animationDirection = Direction.RIGHT;
            stateTime = 0;
        }
        else if(animationDirection == Direction.RIGHT && d == Direction.LEFT)
        {
            animationDirection = Direction.LEFT;
            stateTime = 0;
        }
        if(d == Direction.RIGHT)
        {
            dx = 1;
            dy = 0;
        }
        else if(d == Direction.LEFT)
        {
            dx = -1;
            dy = 0;
        }
//        else if(d == Direction.JUMP)
//        {
//            dx = 0;
//            dy = -1;
//        }
    }


    void Attack (Action a) {
        if (a == Action.NO_ATTACK && act == Action.ATTACK) {
            act = a;
        }
        else if (a == Action.ATTACK && act == Action.NO_ATTACK) {
            act = a;
        }
    }


    void Stop()
    {
        if(state != State.IDLE) {
            dx = 0;
            dy = 0;
            state = State.IDLE;
        }
    }


    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
