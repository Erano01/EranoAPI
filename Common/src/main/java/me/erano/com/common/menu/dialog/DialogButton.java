package me.erano.com.common.menu.dialog;

import java.util.function.Consumer;

//usage:
//new DialogButton<Player>()
//    .label("Kaydet")                                   // görünüm
//    .consumer(click -> click.getPlayer().sendMessage(...)) // davranış

// MenuButton'ın dialog karşılığı: görünüm ve davranış dışarıdan enjekte edilir.
// Consumer'ı olmayan buton sadece dialog'u kapatır.
public class DialogButton<P> {

    private String label = "";
    private String tooltip;
    private int width = 150;
    private Consumer<DialogClick<P>> eventConsumer;

    public DialogButton<P> label(String label) {
        this.label = label;
        return this;
    }

    public DialogButton<P> tooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    /** 1 - 1024, 150 by default. */
    public DialogButton<P> width(int width) {
        this.width = width;
        return this;
    }

    public DialogButton<P> consumer(Consumer<DialogClick<P>> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public String getTooltip() {
        return tooltip;
    }

    public int getWidth() {
        return width;
    }

    public Consumer<DialogClick<P>> getEventConsumer() {
        return eventConsumer;
    }
}
