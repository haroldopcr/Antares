package com.ingeniusapps.antares.javafx;

import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author haroldop
 */
public class FXUIAnimations {

    public FadeTransition fade(Node target, double duration_millis, double from, double to, Interpolator speed_effect, int cycle_count, boolean autostart, boolean autoreverse) {
        FadeTransition fade = new FadeTransition();
        fade.setNode(target);
        fade.setDuration(Duration.millis(duration_millis));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setInterpolator(speed_effect);
        fade.setCycleCount(cycle_count);
        fade.setAutoReverse(autoreverse);

        if (autostart) {
            fade.play();
        }

        return fade;
    }

    public RotateTransition turning(Region target, double duration_millis, Point3D axis, double from, double to, Interpolator speed_effect, int cycle_count, boolean autostart, boolean autoreverse) {
        Rotate rotate = new Rotate();
        rotate.setAxis(axis);
        rotate.setPivotX(target.getPrefWidth());
        rotate.setPivotY(target.getPrefHeight() / 2);
        target.getTransforms().add(rotate);

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(duration_millis), target);
        rotateTransition.setFromAngle(from);
        rotateTransition.setToAngle(to);
        rotateTransition.setAxis(axis);
        rotateTransition.setInterpolator(speed_effect);
        rotateTransition.setCycleCount(cycle_count);
        rotateTransition.setAutoReverse(autoreverse);

        if (autostart) {
            rotateTransition.play();
        }

        return rotateTransition;
    }

    public Timeline shake(Node target, boolean autostart) {
        Random random = new Random();
        Timeline timeline = new Timeline();

        for (int i = 0; i < 10; i++) {
            double randomX = (random.nextDouble() - 0.5) * 10;
            double randomY = (random.nextDouble() - 0.5) * 10;

            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(i * 50),
                    new KeyValue(target.translateXProperty(), randomX),
                    new KeyValue(target.translateYProperty(), randomY)
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        KeyFrame finalKeyFrame = new KeyFrame(
                Duration.millis(500),
                new KeyValue(target.translateXProperty(), 0),
                new KeyValue(target.translateYProperty(), 0)
        );
        timeline.getKeyFrames().add(finalKeyFrame);

        if (autostart) {
            timeline.play();
        }

        return timeline;
    }

    public TranslateTransition slide(Node target, double duration_millis, double from_x, double to_x, double from_y, double to_y, Interpolator speed_effect, int cycle_count, boolean autostart, boolean autoreverse) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(target);
        translateTransition.setDuration(Duration.millis(duration_millis));

        translateTransition.setFromX(from_x);
        translateTransition.setToX(to_x);
        translateTransition.setFromY(from_y);
        translateTransition.setToY(to_y);
        translateTransition.setInterpolator(speed_effect);
        translateTransition.setCycleCount(cycle_count);
        translateTransition.setAutoReverse(autoreverse);

        if (autostart) {
            translateTransition.play();
        }

        return translateTransition;
    }

    public RotateTransition rotate(Node target, double duration_millis, double angle, Interpolator speed_effect, int cycle_count, boolean autostart, boolean autoreverse) {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(duration_millis));
        rotateTransition.setNode(target);
        rotateTransition.setByAngle(angle);
        rotateTransition.setInterpolator(speed_effect);
        rotateTransition.setCycleCount(cycle_count);
        rotateTransition.setAutoReverse(autoreverse);

        if (autostart) {
            rotateTransition.play();
        }

        return rotateTransition;
    }

    public ScaleTransition zoom(Node target, double duration_millis, double scale, Interpolator speed_effect, int cycle_count, boolean autostart, boolean autoreverse) {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(duration_millis));
        scaleTransition.setNode(target);
        scaleTransition.setInterpolator(speed_effect);
        scaleTransition.setByX(scale);
        scaleTransition.setByY(scale);
        scaleTransition.setCycleCount(cycle_count);
        scaleTransition.setAutoReverse(autoreverse);

        if (autostart) {
            scaleTransition.play();
        }

        return scaleTransition;
    }

    /**
     * Efecto "pulse/blink" tipo Macintosh: un parpadeo rápido con ligera
     * contracción.
     *
     * fxAnimations.pulse(card, 200, 0.92, 0.6, Interpolator.EASE_BOTH, 2,
     * true);
     *
     * @param target Node objetivo
     * @param durationMillis Duración total de un pulso (ms) (ida y vuelta si
     * autoReverse=true)
     * @param scaleFactor Factor de escala destino (ej. 0.92 encoge al 92%)
     * @param minOpacity Opacidad mínima durante el pulso (ej. 0.6)
     * @param interpolator Curva de animación (ej. Interpolator.EASE_BOTH)
     * @param cycleCount Repeticiones (ej. 2). Usa Animation.INDEFINITE si lo
     * necesitas.
     * @param autostart Si true, hace play() automáticamente
     * @return ParallelTransition listo para usarse
     */
    public ParallelTransition pulse(Node target, double durationMillis, double scaleFactor, double minOpacity, Interpolator interpolator, int cycleCount, boolean autostart) {

        // --- Escala (X/Y) ---
        ScaleTransition scale = new ScaleTransition(Duration.millis(durationMillis / 2.0), target);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(scaleFactor);
        scale.setToY(scaleFactor);
        scale.setInterpolator(interpolator);
        scale.setAutoReverse(true);

        // --- Opacidad ---
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis / 2.0), target);
        fade.setFromValue(1.0);
        fade.setToValue(minOpacity);
        fade.setInterpolator(interpolator);
        fade.setAutoReverse(true);

        // Correr ambas a la vez
        ParallelTransition pulse = new ParallelTransition(target, scale, fade);
        pulse.setCycleCount(cycleCount);

        // Sanitizar estado al terminar (evita que quede a medias si se interrumpe)
        pulse.setOnFinished(evt -> {
            target.setOpacity(1.0);
            target.setScaleX(1.0);
            target.setScaleY(1.0);
        });

        if (autostart) {
            pulse.playFromStart();
        }
        return pulse;
    }

    /**
     * Efecto "pulse/blink" tipo Macintosh: un parpadeo rápido con ligera
     * contracción.
     *
     * fxAnimations.blink(card, 150, 0.4, Interpolator.LINEAR, 6, true);
     *
     * @param target Node objetivo
     * @param durationMillis Duración total de un pulso (ms) (ida y vuelta si
     * autoReverse=true)
     * @param minOpacity Opacidad mínima durante el pulso (ej. 0.6)
     * @param interpolator Curva de animación (ej. Interpolator.EASE_BOTH)
     * @param cycleCount Repeticiones (ej. 2). Usa Animation.INDEFINITE si lo
     * necesitas.
     * @param autostart Si true, hace play() automáticamente
     * @return FadeTransition listo para usarse
     */
    public FadeTransition blink(Node target, double durationMillis, double minOpacity, Interpolator interpolator, int cycleCount, boolean autostart) {
        FadeTransition blink = new FadeTransition(Duration.millis(durationMillis / 2.0), target);
        blink.setFromValue(1.0);
        blink.setToValue(minOpacity);
        blink.setInterpolator(interpolator);
        blink.setAutoReverse(true);
        blink.setCycleCount(cycleCount);

        blink.setOnFinished(evt -> target.setOpacity(1.0));

        if (autostart) {
            blink.playFromStart();
        }
        return blink;
    }

    /**
     * Efecto "bounce" rebote vertical. contracción.
     *
     * fxAnimations.bounce(card, 300, -15, Interpolator.EASE_OUT, 2, true);
     *
     * @param target Node objetivo
     * @param durationMillis Duración total de un pulso (ms) (ida y vuelta si
     * autoReverse=true)
     * @param offsetY Factor de desplazamiento en Y del efecto de rebote.
     * @param interpolator Curva de animación (ej. Interpolator.EASE_BOTH)
     * @param cycleCount Repeticiones (ej. 2). Usa Animation.INDEFINITE si lo
     * necesitas.
     * @param autostart Si true, hace play() automáticamente
     * @return TranslateTransition listo para usarse
     */
    public TranslateTransition bounce(Node target, double durationMillis, double offsetY, Interpolator interpolator, int cycleCount, boolean autostart) {
        TranslateTransition bounce = new TranslateTransition(Duration.millis(durationMillis / 2.0), target);
        bounce.setFromY(0);
        bounce.setToY(offsetY);
        bounce.setInterpolator(interpolator);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(cycleCount);

        bounce.setOnFinished(evt -> target.setTranslateY(0));

        if (autostart) {
            bounce.playFromStart();
        }
        return bounce;
    }

    /**
     * Efecto "Wiggle" pequeña sacudida horizontal. contracción.
     *
     * fxAnimations.wiggle(card, 100, 8, Interpolator.EASE_IN, 4, true);
     *
     * @param target Node objetivo
     * @param durationMillis Duración total de un pulso (ms) (ida y vuelta si
     * autoReverse=true)
     * @param offsetX Factor de desplazamiento en X del efecto de sacudida a los
     * lados.
     * @param interpolator Curva de animación (ej. Interpolator.EASE_BOTH)
     * @param cycleCount Repeticiones (ej. 2). Usa Animation.INDEFINITE si lo
     * necesitas.
     * @param autostart Si true, hace play() automáticamente
     * @return TranslateTransition listo para usarse
     */
    public TranslateTransition wiggle(Node target, double durationMillis, double offsetX, Interpolator interpolator, int cycleCount, boolean autostart) {
        TranslateTransition wiggle = new TranslateTransition(Duration.millis(durationMillis / 2.0), target);
        wiggle.setFromX(0);
        wiggle.setToX(offsetX);
        wiggle.setInterpolator(interpolator);
        wiggle.setAutoReverse(true);
        wiggle.setCycleCount(cycleCount);

        wiggle.setOnFinished(evt -> target.setTranslateX(0));

        if (autostart) {
            wiggle.playFromStart();
        }
        return wiggle;
    }

    /**
     * Efecto "Glow" efecto brillo aumentando opacidad + escala suave.
     *
     * fxAnimations.glow(card, 250, 1.1, 1.3, Interpolator.EASE_BOTH, 2, true);
     *
     * @param target Node objetivo
     * @param durationMillis Duración total de un pulso (ms) (ida y vuelta si
     * autoReverse=true)
     * @param scaleFactor Factor de escala destino (ej. 0.92 encoge al 92%)
     * @param maxOpacity Opacidad máxima durante el pulso (ej. 0.6)
     * @param interpolator Curva de animación (ej. Interpolator.EASE_BOTH)
     * @param cycleCount Repeticiones (ej. 2). Usa Animation.INDEFINITE si lo
     * necesitas.
     * @param autostart Si true, hace play() automáticamente
     * @return ParallelTransition listo para usarse
     */
    public ParallelTransition glow(Node target, double durationMillis, double scaleFactor, double maxOpacity, Interpolator interpolator, int cycleCount, boolean autostart) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(durationMillis / 2.0), target);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(scaleFactor);
        scale.setToY(scaleFactor);
        scale.setInterpolator(interpolator);
        scale.setAutoReverse(true);

        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis / 2.0), target);
        fade.setFromValue(1.0);
        fade.setToValue(maxOpacity);
        fade.setInterpolator(interpolator);
        fade.setAutoReverse(true);

        ParallelTransition glow = new ParallelTransition(target, scale, fade);
        glow.setCycleCount(cycleCount);

        glow.setOnFinished(evt -> {
            target.setOpacity(1.0);
            target.setScaleX(1.0);
            target.setScaleY(1.0);
        });

        if (autostart) {
            glow.playFromStart();
        }
        return glow;
    }
}
