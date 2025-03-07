package BlackLightRelic.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class TextureUtils {

    public static TextureAtlas.AtlasRegion resizeTexture(TextureAtlas.AtlasRegion originalRegion, int newWidth, int newHeight) {
        // 获取原始纹理数据
        TextureData textureData = originalRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // 获取原始像素图
        Pixmap originalPixmap = textureData.consumePixmap();

        // 创建新的像素图并缩放
        Pixmap resizedPixmap = new Pixmap(newWidth, newHeight, originalPixmap.getFormat());
        resizedPixmap.drawPixmap(originalPixmap,
            0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
            0, 0, newWidth, newHeight);

        // 创建新的纹理
        Texture resizedTexture = new Texture(resizedPixmap);

        // 释放原始像素图
        originalPixmap.dispose();
        resizedPixmap.dispose();

        // 返回新的AtlasRegion
        return new TextureAtlas.AtlasRegion(resizedTexture, 0, 0, newWidth, newHeight);
    }
}

