package client.cardWindow.Animation;

import client.cardWindow.Animation.TimeLines.TimelineFunction;
import client.cardWindow.Cards.Card;

public class Animation {
    private Card card;
    private float frame;
    private float time;
    private TimelineFunction timelineFunction;
    private AnimFunction animation;
    public Animation(Card target, float time, TimelineFunction tlfunc, AnimFunction func) {
        this.frame = 0;
        this.card = target;
        this.animation = func;
        this.time = time;
        this.timelineFunction = tlfunc;
    }
    public boolean play(float timing){
        this.frame = (this.frame + timing)/* % this.time*/;
        float dt = timelineFunction.func(frame/time);
        if(dt>=1){return true;}
        //this.frame += timing;
        animation.func(card, dt);
        return false;
    }
    public void setFrame(float frame){
        this.frame = frame;
    }
}

