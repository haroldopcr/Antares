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
 * Proporciona utilidades de animación para nodos JavaFX.
 *
 * <p>Esta clase centraliza la creación de efectos visuales comunes para
 * componentes de interfaz, incluyendo transiciones de opacidad, traslación,
 * rotación, escala y combinaciones de estas. Los métodos retornan las
 * transiciones configuradas para que puedan ser reutilizadas, personalizadas o
 * controladas externamente por el consumidor.</p>
 *
 * <p>La mayoría de los métodos permiten indicar si la animación debe iniciar
 * automáticamente al ser creada.</p>
 */
public class FXUIAnimations {

    /**
     * Crea una animación de opacidad sobre un nodo.
     *
     * <p>La transición se configura con duración, interpolador, cantidad de ciclos
     * y comportamiento de auto-reversa. Si {@code autostart} es {@code true},
     * la animación se reproduce inmediatamente.</p>
     *
     * @param target nodo objetivo de la animación
     * @param duration_millis duración de la animación en milisegundos
     * @param from valor inicial de opacidad solicitado por el consumidor
     * @param to valor final de opacidad solicitado por el consumidor
     * @param speed_effect interpolador a utilizar
     * @param cycle_count cantidad de ciclos
     * @param autostart indica si la animación debe iniciar automáticamente
     * @param autoreverse indica si la animación debe reproducirse en reversa al finalizar cada ciclo
     * @return transición de opacidad configurada
     */
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

    /**
     * Crea una animación de rotación para una región JavaFX alrededor de un eje 3D.
     *
     * <p>Antes de crear la transición, el método agrega una transformación
     * {@link Rotate} al nodo objetivo y define pivotes basados en el tamaño
     * preferido de la región.</p>
     *
     * @param target región objetivo de la animación
     * @param duration_millis duración de la animación en milisegundos
     * @param axis eje de rotación
     * @param from ángulo inicial
     * @param to ángulo final
     * @param speed_effect interpolador a utilizar
     * @param cycle_count cantidad de ciclos
     * @param autostart indica si la animación debe iniciar automáticamente
     * @param autoreverse indica si la animación debe reproducirse en reversa al finalizar cada ciclo
     * @return transición de rotación configurada
     */
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

    /**
     * Crea un efecto de sacudida aleatoria en los ejes X e Y.
     *
     * <p>La animación genera desplazamientos aleatorios cortos durante varios
     * cuadros y luego retorna el nodo a su posición original.</p>
     *
     * @param target nodo objetivo
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return línea de tiempo configurada para el efecto de sacudida
     */
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

    /**
     * Crea una animación de deslizamiento en los ejes X e Y.
     *
     * @param target nodo objetivo
     * @param duration_millis duración de la animación en milisegundos
     * @param from_x posición inicial en X
     * @param to_x posición final en X
     * @param from_y posición inicial en Y
     * @param to_y posición final en Y
     * @param speed_effect interpolador a utilizar
     * @param cycle_count cantidad de ciclos
     * @param autostart indica si la animación debe iniciar automáticamente
     * @param autoreverse indica si la animación debe reproducirse en reversa al finalizar cada ciclo
     * @return transición de traslación configurada
     */
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

    /**
     * Crea una animación de rotación incremental sobre un nodo.
     *
     * @param target nodo objetivo
     * @param duration_millis duración de la animación en milisegundos
     * @param angle ángulo incremental de rotación
     * @param speed_effect interpolador a utilizar
     * @param cycle_count cantidad de ciclos
     * @param autostart indica si la animación debe iniciar automáticamente
     * @param autoreverse indica si la animación debe reproducirse en reversa al finalizar cada ciclo
     * @return transición de rotación configurada
     */
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

    /**
     * Crea una animación de zoom uniforme en X e Y.
     *
     * @param target nodo objetivo
     * @param duration_millis duración de la animación en milisegundos
     * @param scale variación de escala a aplicar
     * @param speed_effect interpolador a utilizar
     * @param cycle_count cantidad de ciclos
     * @param autostart indica si la animación debe iniciar automáticamente
     * @param autoreverse indica si la animación debe reproducirse en reversa al finalizar cada ciclo
     * @return transición de escala configurada
     */
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
     * Crea un efecto de pulso o parpadeo con ligera contracción.
     *
     * <p>El efecto combina una transición de escala y una de opacidad ejecutadas
     * en paralelo. Al finalizar, el estado visual del nodo se restablece para
     * evitar que quede en una condición intermedia.</p>
     *
     * @param target nodo objetivo
     * @param durationMillis duración total del pulso en milisegundos
     * @param scaleFactor factor de escala destino
     * @param minOpacity opacidad mínima durante el pulso
     * @param interpolator curva de animación
     * @param cycleCount cantidad de repeticiones
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return transición paralela configurada
     */
    public ParallelTransition pulse(Node target, double durationMillis, double scaleFactor, double minOpacity, Interpolator interpolator, int cycleCount, boolean autostart) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(durationMillis / 2.0), target);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(scaleFactor);
        scale.setToY(scaleFactor);
        scale.setInterpolator(interpolator);
        scale.setAutoReverse(true);

        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis / 2.0), target);
        fade.setFromValue(1.0);
        fade.setToValue(minOpacity);
        fade.setInterpolator(interpolator);
        fade.setAutoReverse(true);

        ParallelTransition pulse = new ParallelTransition(target, scale, fade);
        pulse.setCycleCount(cycleCount);

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
     * Crea un efecto de parpadeo rápido basado en opacidad.
     *
     * <p>Al finalizar, la opacidad del nodo se restablece a {@code 1.0}.</p>
     *
     * @param target nodo objetivo
     * @param durationMillis duración total del efecto en milisegundos
     * @param minOpacity opacidad mínima durante el parpadeo
     * @param interpolator curva de animación
     * @param cycleCount cantidad de repeticiones
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return transición de opacidad configurada
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
     * Crea un efecto de rebote vertical.
     *
     * <p>Al finalizar, el desplazamiento vertical del nodo se restablece a cero.</p>
     *
     * @param target nodo objetivo
     * @param durationMillis duración total del efecto en milisegundos
     * @param offsetY desplazamiento vertical del rebote
     * @param interpolator curva de animación
     * @param cycleCount cantidad de repeticiones
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return transición de traslación vertical configurada
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
     * Crea un efecto de sacudida horizontal corta.
     *
     * <p>Al finalizar, el desplazamiento horizontal del nodo se restablece a cero.</p>
     *
     * @param target nodo objetivo
     * @param durationMillis duración total del efecto en milisegundos
     * @param offsetX desplazamiento horizontal máximo
     * @param interpolator curva de animación
     * @param cycleCount cantidad de repeticiones
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return transición de traslación horizontal configurada
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
     * Crea un efecto de brillo combinado con aumento suave de escala y opacidad.
     *
     * <p>El efecto combina una transición de escala y una transición de opacidad
     * ejecutadas en paralelo. Al finalizar, el estado visual del nodo se
     * restablece a sus valores base.</p>
     *
     * @param target nodo objetivo
     * @param durationMillis duración total del efecto en milisegundos
     * @param scaleFactor factor de escala destino
     * @param maxOpacity opacidad máxima a aplicar durante el efecto
     * @param interpolator curva de animación
     * @param cycleCount cantidad de repeticiones
     * @param autostart indica si la animación debe iniciar automáticamente
     * @return transición paralela configurada
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