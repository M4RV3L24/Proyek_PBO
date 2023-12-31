package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player2{
    public Player2() {
        this.generatePlayerAnimation();
    }

    Double HP = 100.0;
    Double ratioHP = 1.0;

    Double dmg = 10.0;

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
        NO_ATTACK,
        HITTED
    }

    Animation<TextureRegion> idleLeftAnimation, runLeftAnimation, idleRightAnimation, runRightAnimation, runRightJump, runLeftJump, runRightAttack, runLeftAttack,
            runRightDeath, runLeftDeath, runRightHitted, runLeftHitted, runRightFall, runLeftFall;

    MyGdxGame app;

    public void generatePlayerAnimation()
    {
        app = (MyGdxGame) Gdx.app.getApplicationListener();
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

        if (HP <= 0) {
            if (animationDirection == Direction.LEFT) {
                currentFrame = runLeftDeath.getKeyFrame(stateTime, false);
            } else if (animationDirection == Direction.RIGHT) {
                currentFrame = runRightDeath.getKeyFrame(stateTime, false);
            }
        }
        else {
            if (state == State.RUN && animationDirection == Direction.LEFT && act == Action.NO_ATTACK)
                currentFrame = runLeftAnimation.getKeyFrame(stateTime, true);
            else if (state == State.RUN && animationDirection == Direction.RIGHT && act == Action.NO_ATTACK)
                currentFrame = runRightAnimation.getKeyFrame(stateTime, true);
            else if (state == State.IDLE && animationDirection == Direction.LEFT && act == Action.NO_ATTACK)
                currentFrame = idleLeftAnimation.getKeyFrame(stateTime, true);
            else if (state == State.IDLE && animationDirection == Direction.RIGHT && act == Action.NO_ATTACK)
                currentFrame = idleRightAnimation.getKeyFrame(stateTime, true);
            else if ((state == State.IDLE || state == State.RUN || state == State.JUMP) && animationDirection == Direction.RIGHT && act == Action.ATTACK) {
                currentFrame = runRightAttack.getKeyFrame(stateTime, true);
                if (state == State.RUN) Stop();
                if (stateTime >= Gdx.graphics.getDeltaTime() * 17) {
                    if (app.p2.canHit(app.p1)) {
                        app.p1.setHP(app.p1.getHP()-app.p2.getDmg());
                        app.fontcache1.setText(String.format("Player 1 HP: %.2f",app.p1.getHP()), 120, 500);
//                        app.p1_hp.setSize((float) (450 * app.p1.getHP()/100.0), app.p1_hp.getHeight());
                        app.p1.doAction(Player1.Action.HITTED);
                    }
                    doAction(Action.NO_ATTACK);
                }
            } else if ((state == State.IDLE || state == State.RUN || state == State.JUMP) && animationDirection == Direction.LEFT && act == Action.ATTACK) {
                currentFrame = runLeftAttack.getKeyFrame(stateTime, true);
                if (state == State.RUN) Stop();
                if (stateTime >= Gdx.graphics.getDeltaTime() * 17) {
                    if (app.p2.canHit(app.p1)) {
                        app.p1.setHP(app.p1.getHP()-app.p2.getDmg());
                        app.fontcache1.setText(String.format("Player 1 HP: %.2f",app.p1.getHP()), 120, 500);
//                        app.p1_hp.setSize((float) (450*app.p1.getHP()/100.0), app.p1_hp.getHeight());
                        app.p1.doAction(Player1.Action.HITTED);
                    }
                    doAction(Action.NO_ATTACK);
                }
            } else if ((state == State.JUMP) && animationDirection == Direction.RIGHT) {
                currentFrame = runRightJump.getKeyFrame(stateTime, true);
            } else if (state == State.FALL && animationDirection == Direction.RIGHT) {
                currentFrame = runRightFall.getKeyFrame(stateTime, true);
            } else if ((state == State.JUMP) && animationDirection == Direction.LEFT) {
                currentFrame = runLeftJump.getKeyFrame(stateTime, true);
            } else if (state == State.FALL && animationDirection == Direction.LEFT) {
                currentFrame = runLeftFall.getKeyFrame(stateTime, true);
            }
            if (act == Action.HITTED) {
                if (animationDirection == Direction.LEFT) {
                    currentFrame = runLeftHitted.getKeyFrame(stateTime, true);
                }
                else if (animationDirection == Direction.RIGHT) {
                    currentFrame = runRightHitted.getKeyFrame(stateTime, true);
                }
                if (stateTime >= Gdx.graphics.getDeltaTime() * 9) doAction(Action.NO_ATTACK);
            }
        }

        batch.draw(currentFrame, x-100, y-100);
    }

    public void update()
    {
        ratioHP = HP/100.0;
        float elapsed = Gdx.graphics.getDeltaTime();
        stateTime += elapsed;
        if (HP > 0) {


            x += dx * speed * elapsed;
            if (x > MyGdxGame.WORLD_WIDTH - 20) {
                x = MyGdxGame.WORLD_WIDTH - 20;
                this.Stop();
            } else if (x < 20) {
                x = 20;
                this.Stop();
            }


            y += dy * speed * elapsed;
            if (y > 200 + 120) {
                y = 300;
                dy = -1;
                this.Jump(State.FALL);
            } else if (y < 140) {
                y = 140;
                this.Stop();
            }
            if (act == Action.HITTED) {
                if (app.p1.direction == Player1.Direction.RIGHT) {
                    x+= speed * elapsed * 0.8;
                }
                else if (app.p1.direction == Player1.Direction.LEFT) {
                    x-= speed * elapsed * 0.8;
                }

            }
        }
    }

    void Jump (State s){
        if (HP > 0) {
            if ((state == State.IDLE || state == State.FALL || state == State.RUN) && s == State.JUMP) {
                state = State.JUMP;
                dy = 1;
            }
            if ((state == State.JUMP || state == State.IDLE || state == State.RUN) && s == State.FALL) {
                state = State.FALL;
                dy = -1;
            }
        }
    }

    public void setMove(Direction d)
    {
        if (HP > 0) {
            direction = d;

            if (animationDirection == Direction.LEFT && d == Direction.RIGHT) {
                animationDirection = Direction.RIGHT;
                stateTime = 0;
            } else if (animationDirection == Direction.RIGHT && d == Direction.LEFT) {
                animationDirection = Direction.LEFT;
                stateTime = 0;
            }
            if (d == Direction.RIGHT) {
                dx = 1;
                dy = 0;
            } else if (d == Direction.LEFT) {
                dx = -1;
                dy = 0;
            }

            if (y == 140) {
                state = State.RUN; // update state dan arah animasi
            } else {
                if (state == State.FALL) {
                    dy = -1;
                }
                if (state == State.JUMP) {
                    dy = 1;
                }
            }
        }
    }


    void doAction (Action a) {
        float elapsed = Gdx.graphics.getDeltaTime();
        stateTime += elapsed;
        if (HP > 0) {
            if (a == Action.NO_ATTACK && (act == Action.ATTACK || act == Action.HITTED)) {
                act = a;
                stateTime = 0;
            }
            else if (a == Action.ATTACK && (act == Action.NO_ATTACK || act == Action.HITTED)) {
                act = a;
                stateTime = 0;
            }
            else if (a == Action.HITTED && (act == Action.NO_ATTACK || act == Action.ATTACK)) {
                act = a;
                stateTime = 0;
            }
        }
    }


    void Stop()
    {
        if(state != State.IDLE && y == 140) {
            dx = 0;
            dy = 0;
            state = State.IDLE;
        }
        else if (y > 140) {
            state = State.FALL;
            dy = -1;
        }
    }


    public Double getHP() {
        return HP;
    }

    public void setHP(Double HP) {
        this.HP = HP;
        if (this.HP < 0) this.HP = 0.0;
    }

    public Double getDmg() {
        return dmg;
    }

    public void setDmg(Double dmg) {
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
    public boolean canHit (Player1 p1) {
        if (HP > 0) {
            if (act == Action.NO_ATTACK) {
                return false;
            }
            else if (act == Action.ATTACK) {
                float jarak = 0;
                if (animationDirection == Direction.RIGHT) {
                    jarak = p1.getX() - x;
                } else if (animationDirection == Direction.LEFT) {
                    jarak = x - p1.getX();
                }
                return jarak <= 100 && jarak > 50 && Math.abs(p1.getY() - y) <= 10;
            }
        }
        return false;
    }

    public Action getAct() {
        return act;
    }

    public void setAct(Action act) {
        this.act = act;
    }
}
