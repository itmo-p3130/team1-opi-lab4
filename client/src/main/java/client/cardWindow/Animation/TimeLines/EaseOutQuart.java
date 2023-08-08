package client.cardWindow.Animation.TimeLines;

public class EaseOutQuart implements TimelineFunction{
    @Override
    public float func(float frame){
        return (float)(1 - Math.pow(1-frame,4));
    }
}
