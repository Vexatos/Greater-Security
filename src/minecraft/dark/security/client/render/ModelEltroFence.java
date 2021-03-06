// Date: 6/1/2013 2:50:47 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package dark.security.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelEltroFence extends ModelBase
{
    // fields
    ModelRenderer centerMast;
    ModelRenderer Top;
    ModelRenderer Top2;
    ModelRenderer RightC;
    ModelRenderer RightCTop;
    ModelRenderer RightFence;
    ModelRenderer RightCBot;
    ModelRenderer FrontC;
    ModelRenderer FrontCTop;
    ModelRenderer FrontFence;
    ModelRenderer FrontCBot;
    ModelRenderer LeftC;
    ModelRenderer LeftCTop;
    ModelRenderer LeftFence;
    ModelRenderer LeftCBot;
    ModelRenderer BackC;
    ModelRenderer BackCTop;
    ModelRenderer BacktFence;
    ModelRenderer BackCBot;

    public ModelEltroFence()
    {
        textureWidth = 64;
        textureHeight = 64;

        centerMast = new ModelRenderer(this, 0, 0);
        centerMast.addBox(-2.5F, 0F, -2.5F, 5, 16, 5);
        centerMast.setRotationPoint(0F, 8F, 0F);
        centerMast.setTextureSize(64, 64);
        centerMast.mirror = true;
        setRotation(centerMast, 0F, 0F, 0F);
        Top = new ModelRenderer(this, 30, 23);
        Top.addBox(-1F, -1F, -1F, 2, 1, 2);
        Top.setRotationPoint(0F, 8F, 0F);
        Top.setTextureSize(64, 64);
        Top.mirror = true;
        setRotation(Top, 0F, 0F, 0F);
        Top2 = new ModelRenderer(this, 16, 35);
        Top2.addBox(-1.5F, -2F, -1.5F, 3, 1, 3);
        Top2.setRotationPoint(0F, 8F, 0F);
        Top2.setTextureSize(64, 64);
        Top2.mirror = true;
        setRotation(Top2, 0F, 0F, 0F);
        RightC = new ModelRenderer(this, 0, 23);
        RightC.addBox(-3.5F, 0F, -1F, 1, 16, 2);
        RightC.setRotationPoint(0F, 8F, 0F);
        RightC.setTextureSize(64, 64);
        RightC.mirror = true;
        setRotation(RightC, 0F, 0F, 0F);
        RightCTop = new ModelRenderer(this, 16, 30);
        RightCTop.addBox(-8F, 0F, -1F, 5, 1, 2);
        RightCTop.setRotationPoint(0F, 8F, 0F);
        RightCTop.setTextureSize(64, 64);
        RightCTop.mirror = true;
        setRotation(RightCTop, 0F, 0F, 0F);
        RightFence = new ModelRenderer(this, 23, 3);
        RightFence.addBox(-8F, -1F, 0F, 5, 16, 0);
        RightFence.setRotationPoint(0F, 9F, 0F);
        RightFence.setTextureSize(64, 64);
        RightFence.mirror = true;
        setRotation(RightFence, 0F, 0F, 0F);
        RightCBot = new ModelRenderer(this, 16, 30);
        RightCBot.addBox(-8F, 15F, -1F, 5, 1, 2);
        RightCBot.setRotationPoint(0F, 8F, 0F);
        RightCBot.setTextureSize(64, 64);
        RightCBot.mirror = true;
        setRotation(RightCBot, 0F, 0F, 0F);
        FrontC = new ModelRenderer(this, 0, 23);
        FrontC.addBox(-3.5F, 0F, -1F, 1, 16, 2);
        FrontC.setRotationPoint(0F, 8F, 0F);
        FrontC.setTextureSize(64, 64);
        FrontC.mirror = true;
        setRotation(FrontC, 0F, -1.570796F, 0F);
        FrontCTop = new ModelRenderer(this, 16, 30);
        FrontCTop.addBox(-8F, 0F, -1F, 5, 1, 2);
        FrontCTop.setRotationPoint(0F, 8F, 0F);
        FrontCTop.setTextureSize(64, 64);
        FrontCTop.mirror = true;
        setRotation(FrontCTop, 0F, -1.570796F, 0F);
        FrontFence = new ModelRenderer(this, 23, 3);
        FrontFence.addBox(-8F, -1F, 0F, 5, 16, 0);
        FrontFence.setRotationPoint(0F, 9F, 0F);
        FrontFence.setTextureSize(64, 64);
        FrontFence.mirror = true;
        setRotation(FrontFence, 0F, -1.570796F, 0F);
        FrontCBot = new ModelRenderer(this, 16, 30);
        FrontCBot.addBox(-8F, 15F, -1F, 5, 1, 2);
        FrontCBot.setRotationPoint(0F, 8F, 0F);
        FrontCBot.setTextureSize(64, 64);
        FrontCBot.mirror = true;
        setRotation(FrontCBot, 0F, -1.570796F, 0F);
        LeftC = new ModelRenderer(this, 0, 23);
        LeftC.addBox(-3.5F, 0F, -1F, 1, 16, 2);
        LeftC.setRotationPoint(0F, 8F, 0F);
        LeftC.setTextureSize(64, 64);
        LeftC.mirror = true;
        setRotation(LeftC, 0F, -3.141593F, 0F);
        LeftCTop = new ModelRenderer(this, 16, 30);
        LeftCTop.addBox(-8F, 0F, -1F, 5, 1, 2);
        LeftCTop.setRotationPoint(0F, 8F, 0F);
        LeftCTop.setTextureSize(64, 64);
        LeftCTop.mirror = true;
        setRotation(LeftCTop, 0F, 3.141593F, 0F);
        LeftFence = new ModelRenderer(this, 23, 3);
        LeftFence.addBox(-8F, -1F, 0F, 5, 16, 0);
        LeftFence.setRotationPoint(0F, 9F, 0F);
        LeftFence.setTextureSize(64, 64);
        LeftFence.mirror = true;
        setRotation(LeftFence, 0F, 3.141593F, 0F);
        LeftCBot = new ModelRenderer(this, 16, 30);
        LeftCBot.addBox(-8F, 15F, -1F, 5, 1, 2);
        LeftCBot.setRotationPoint(0F, 8F, 0F);
        LeftCBot.setTextureSize(64, 64);
        LeftCBot.mirror = true;
        setRotation(LeftCBot, 0F, 3.141593F, 0F);
        BackC = new ModelRenderer(this, 0, 23);
        BackC.addBox(-3.5F, 0F, -1F, 1, 16, 2);
        BackC.setRotationPoint(0F, 8F, 0F);
        BackC.setTextureSize(64, 64);
        BackC.mirror = true;
        setRotation(BackC, 0F, 1.570796F, 0F);
        BackCTop = new ModelRenderer(this, 16, 30);
        BackCTop.addBox(-8F, 0F, -1F, 5, 1, 2);
        BackCTop.setRotationPoint(0F, 8F, 0F);
        BackCTop.setTextureSize(64, 64);
        BackCTop.mirror = true;
        setRotation(BackCTop, 0F, 1.570796F, 0F);
        BacktFence = new ModelRenderer(this, 23, 3);
        BacktFence.addBox(-8F, -1F, 0F, 5, 16, 0);
        BacktFence.setRotationPoint(0F, 9F, 0F);
        BacktFence.setTextureSize(64, 64);
        BacktFence.mirror = true;
        setRotation(BacktFence, 0F, 1.570796F, 0F);
        BackCBot = new ModelRenderer(this, 16, 30);
        BackCBot.addBox(-8F, 15F, -1F, 5, 1, 2);
        BackCBot.setRotationPoint(0F, 8F, 0F);
        BackCBot.setTextureSize(64, 64);
        BackCBot.mirror = true;
        setRotation(BackCBot, 0F, 1.570796F, 0F);
    }

    public void render(float f5, boolean top)
    {
        centerMast.render(f5);

        if (top)
        {
            Top.render(f5);
            Top2.render(f5);
        }

    }

    public void renderFence(float f5, boolean top, boolean bot, int side)
    {
        if (side == 0)
        {
            RightC.render(f5);
            RightFence.render(f5);

            if (top)
            {
                RightCTop.render(f5);
            }
            if (bot)
            {
                RightCBot.render(f5);

            }
        }
        if (side == 1)
        {
            FrontC.render(f5);
            FrontFence.render(f5);

            if (top)
            {
                FrontCTop.render(f5);
            }
            if (bot)
            {
                FrontCBot.render(f5);
            }
        }
        if (side == 2)
        {
            LeftC.render(f5);
            LeftFence.render(f5);

            if (top)
            {
                LeftCTop.render(f5);
            }
            if (bot)
            {
                LeftCBot.render(f5);
            }
        }
        if (side == 3)
        {
            BackC.render(f5);
            BacktFence.render(f5);

            if (top)
            {
                BackCTop.render(f5);
            }
            if (bot)
            {
                BackCBot.render(f5);
            }
        }

    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
