package com.minecolonies.blockout;

import com.minecolonies.blockout.views.Window;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;

/**
 * An OverlayView is a full screen View which is displayed on top of the window.
 */
public class OverlayView extends View
{
    /**
     * Constructs a barebones View.
     */
    public OverlayView()
    {
        super();
    }

    /**
     * Constructs a OverlayView from PaneParams.
     *
     * @param params Params for the View.
     */
    public OverlayView(final PaneParams params)
    {
        super(params);
    }

    /**
     * hide the view when click on.
     */
    @Override
    public void click(final int mx, final int my)
    {
        final int mxChild = mx - x - padding;
        final int myChild = my - y - padding;
        final Pane clickedPane = findPaneForClick(mxChild, myChild);
        if (clickedPane != null)
        {
            clickedPane.click(mxChild, myChild);
        }
        else
        {
            hide();
        }
    }

    @Override
    public void handleClick(final int mx, final int my)
    {
        Log.getLogger().info("OverlayView::handleClick()");
        setVisible(false);
    }



    /**
     * Called when a key is pressed.
     * hide the view when ESC is pressed.
     *
     * @param ch  the character.
     * @param key the key.
     * @return false at all times - do nothing.
     */
    @Override
    public boolean onKeyTyped(final char ch, final int key)
    {
        Log.getLogger().info("OverlayView::onKeyTyped()");
        if (isVisible() && key == Keyboard.KEY_ESCAPE)
        {
            setVisible(false);
            return true;
        }

        return super.onKeyTyped(ch, key);
    }
}
