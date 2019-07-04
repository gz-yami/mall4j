/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleCaptcha extends LineCaptcha{
	
	private static final long serialVersionUID = -9042552338521307038L;
	
	private static final String CAPTCHA_CODE = "abcdefhjkmnpqrstuvwxyz2345678";

	public SimpleCaptcha(int width, int height, int codeCount, int interfereCount) {

		super(width, height, codeCount, interfereCount);
	}

	@Override
	protected void generateCode() {
		this.code = RandomUtil.randomString(CAPTCHA_CODE,this.generator.getLength());
	}

	@Override
	public Image createImage(String code) {
		// 图像buffer
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final ThreadLocalRandom random = RandomUtil.getRandom();
		final Graphics2D g = ImageUtil.createGraphics(image, new Color(249,249,249));

		// 干扰线
		drawInterfere(g, random);

		// 创建字体
		g.setFont(this.font);
		final FontMetrics metrics = g.getFontMetrics();
		int minY = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();
		// 文字
		final int len = this.generator.getLength();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
			g.setColor(ImageUtil.randomColor(random));
			g.drawString(String.valueOf(code.charAt(i)), i * charWidth, RandomUtil.randomInt(minY, this.height));
		}

		return image;
	}

	/**
	 * 绘制干扰线
	 *
	 * @param g {@link Graphics2D}画笔
	 * @param random 随机对象
	 */
	private void drawInterfere(Graphics2D g, ThreadLocalRandom random) {
		// 干扰线
		for (int i = 0; i < this.interfereCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			g.setColor(ImageUtil.randomColor(random));
			g.drawLine(xs, ys, xe, ye);
		}
	}

}
