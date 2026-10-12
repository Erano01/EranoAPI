package me.erano.com.api.menu.dialog;

/** A slider from {@link #getStart()} to {@link #getEnd()}. */
public class NumberInput extends DialogInput {

    private final float start;
    private final float end;
    private Float step;
    private Float initial;

    NumberInput(String key, String label, float start, float end) {
        super(key, label);
        this.start = start;
        this.end = end;
    }

    public NumberInput step(float step) {
        this.step = step;
        return this;
    }

    public NumberInput initial(float initial) {
        this.initial = initial;
        return this;
    }

    public float getStart() {
        return start;
    }

    public float getEnd() {
        return end;
    }

    /** Null: any value. */
    public Float getStep() {
        return step;
    }

    /** Null: the middle. */
    public Float getInitial() {
        return initial;
    }
}
