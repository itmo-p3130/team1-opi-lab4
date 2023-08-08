package client.cardWindow.Animation.TimeLines;

public class FastStartFastStop implements TimelineFunction{
    @Override
    public float func(float frame){

        return frame < 0.5 ? 16 * frame * frame * frame * frame * frame : (float) (1 - Math.pow(-2 * frame + 2, 5) / 2);
    }
}
