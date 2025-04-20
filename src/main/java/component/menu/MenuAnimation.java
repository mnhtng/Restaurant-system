package main.java.component.menu;

import com.formdev.flatlaf.util.Animator;

import java.awt.*;
import java.util.HashMap;

/**
 * @author MnhTng
 * @Package main.java.component.menu
 * @date 4/19/2025 3:34 PM
 * @Copyright tùng
 */

// This class is responsible for animating the menu items when they are shown or hidden.
public class MenuAnimation {
    // Store the active animations for each menu item
    private static final HashMap<MenuItem, Animator> hash = new HashMap<>();

    // Animates the specified menu item to show or hide it.
    public static void animate(MenuItem menu, boolean show) {
        // Check if an animation is already running for the menu item and stop it if necessary to avoid conflicts, lag.
        if (hash.containsKey(menu) && hash.get(menu).isRunning()) {
            hash.get(menu).stop();
        }

        // Set the visibility state of the menu item.
        menu.setMenuShow(show);

        // Create a new animator for the menu item.
        Animator animator = new Animator(400, new Animator.TimingTarget() {
            @Override
            public void timingEvent(float f) {
                // Update the animation progress based on the visibility state.
                if (show) {
                    menu.setAnimate(f);
                } else {
                    menu.setAnimate(1f - f);
                }
                menu.revalidate();
            }

            @Override
            public void end() {
                hash.remove(menu);
            }
        });

        animator.setResolution(1);
        animator.setInterpolator((float f) -> (float) (1 - Math.pow(1 - f, 3)));
        animator.start();
        hash.put(menu, animator);
    }
}
