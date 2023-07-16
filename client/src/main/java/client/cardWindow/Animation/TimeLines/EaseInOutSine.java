package client.cardWindow.Animation.TimeLines;

public class EaseInOutSine implements TimelineFunction{
    @Override
    public float func(float frame){
        return (float)-(Math.cos(Math.PI * frame)-1)/2;
    }
}
