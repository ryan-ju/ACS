/**
 * 
 */
package com.asys.constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.Element;
import com.asys.editor.model.ElementVisitor;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.Fanout;
import com.asys.editor.model.InputGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.NotGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.OutputGate;
import com.asys.editor.model.XorGate;

/**
 * @author ryan
 * 
 */
public class ImageFetcher {
	private static ImageFetcher instance;
	public static BufferedImage INPUT_IMG, OUTPUT_IMG, ENV_IMG, NOT_IMG,
			AND_IMG, OR_IMG, NAND_IMG, NOR_IMG, XOR_IMG, C_IMG;
	private static final int m = Constant.GRID_SIZE;
	// private static final int c = Constant.GATE_MARGIN;
	private static final float r = Constant.GATE_MARGIN_RATIO;
	private static final Color br_c = Constant.ELEMENT_BORDER_CLR,
			bg_c = Constant.ELEMENT_BACKGROUND_COLOR;
	private static ElementImageFetcher eif;

	public static ImageFetcher getInstance() {
		if (instance == null) {
			instance = new ImageFetcher();
		}
		return instance;
	}

	private ImageFetcher() {
		try {
			INPUT_IMG = ImageIO
					.read(new File(Constant.IMAGE_PATH + "input.png"));
			OUTPUT_IMG = ImageIO.read(new File(Constant.IMAGE_PATH
					+ "output.png"));
			ENV_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "env.png"));
			NOT_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "not.png"));
			AND_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "and.png"));
			OR_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "or.png"));
			NAND_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "nand.png"));
			NOR_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "nor.png"));
			XOR_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "xor.png"));
			C_IMG = ImageIO.read(new File(Constant.IMAGE_PATH + "c.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eif = new ElementImageFetcher();
	}

	/**
	 * This method returns a BufferedImage of 'elt'.
	 * 
	 * @param elt
	 * @param withPins
	 * @return
	 */
	public static BufferedImage getElementImage(Element elt) {
		if (eif == null) {
			eif = new ElementImageFetcher();
		}
		int w = elt.getWidth() * m, h = elt.getHeight() * m, wr = elt
				.getIntrinsicWidth() * m, hr = elt.getIntrinsicHeight() * m;
		BufferedImage buf = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gc = buf.createGraphics();
		AffineTransform newAT = new AffineTransform();
		switch (elt.getOrientation()) {
		case RIGHT:
			break;
		case DOWN:
			newAT.translate(w, 0);
			newAT.quadrantRotate(1);
			break;
		case LEFT:
			newAT.translate(w, h);
			newAT.quadrantRotate(2);
			break;
		case UP:
			newAT.translate(0, h);
			newAT.quadrantRotate(3);
			break;
		}
		gc.setTransform(newAT);
		gc.drawImage(eif.getImage(elt), Math.round(wr * r), Math.round(hr * r),
				Math.round(wr * (1 - 2 * r)), Math.round(hr * (1 - 2 * r)),
				null);
		gc.dispose();
		return buf;
	}

	/**
	 * This class is an auxiliary class for getting the correct image of
	 * different types of Element.
	 * 
	 * @author ryan
	 * 
	 */
	public static class ElementImageFetcher implements ElementVisitor {
		static BufferedImage img;
		static BufferedImage input_img;
		static BufferedImage output_img;
		static BufferedImage env_img;
		static BufferedImage not_img;
		static BufferedImage and_img;
		static BufferedImage or_img;
		static BufferedImage nand_img;
		static BufferedImage nor_img;
		static BufferedImage xor_img;
		static BufferedImage c_img;

		/**
		 * This method returns the image of 'elt', without pins. The orientation
		 * is towards right (outports are on the right).
		 * 
		 * @param elt
		 * @return
		 */
		public BufferedImage getImage(Element elt) {
			elt.accept(this);
			return img;
		}

		@Override
		public void visit(InputGate input) {
			if (input_img == null) {
				int wr = input.getIntrinsicWidth()* m, hr = input
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();

				// Fill a rectangle shape
				gc.setColor(bg_c);
				gc.fillRect(1, 1, wr - 2, hr - 2);
				// Stroke out the boundary of the rectangle
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.drawRect(1, 1, wr - 2, hr - 2);
				// Paint a character
				Font font = new Font("Courier", Font.PLAIN, hr / 3);
				Rectangle2D bound = font.getStringBounds("I",
						gc.getFontRenderContext());
				gc.setFont(font);
				gc.drawString("I", Math.round(wr / 2 - bound.getWidth() / 2),
						Math.round(hr / 2 + bound.getHeight() / 3));
				gc.dispose();
				input_img = buf;
			}
			img = input_img;
		}

		@Override
		public void visit(OutputGate output) {
			if (output_img == null) {
				int wr = output.getIntrinsicWidth() * m, hr = output
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				// Fill a rectangle shape
				gc.setColor(bg_c);
				gc.fillOval(1, 1, wr - 2, hr - 2);
				// Stroke out the boundary of the rectangle
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.drawOval(1, 1, wr - 2, hr - 2);
				// Paint a character
				Font font = new Font("Courier", Font.BOLD, hr / 3);
				Rectangle2D bound = font.getStringBounds("O",
						gc.getFontRenderContext());
				gc.setFont(font);
				gc.drawString("O", Math.round(wr / 2 - bound.getWidth() / 2),
						Math.round(hr / 2 + bound.getHeight() / 3));
				gc.dispose();
				output_img = buf;
			}
			img = output_img;
		}

		@Override
		public void visit(EnvironmentGate env) {
			if (env_img == null) {
				int wr = env.getIntrinsicWidth() * m, hr = env
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				// Fill a rectangle shape
				gc.setColor(bg_c);
				gc.fillRect(1, 1, wr - 2, hr - 2);
				// Stroke out the boundary of the rectangle
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.drawRect(1, 1, wr - 2, hr - 2);
				// Paint a character
				Font font = new Font("Courier", Font.PLAIN, hr / 3);
				Rectangle2D bound = font.getStringBounds("E",
						gc.getFontRenderContext());
				gc.setFont(font);
				gc.drawString("E", Math.round(wr / 2 - bound.getWidth() / 2),
						Math.round(hr / 2 + bound.getHeight() / 3));
				gc.dispose();
				env_img = buf;
			}
			img = env_img;
		}

		@Override
		public void visit(NotGate not) {
			if (not_img == null) {
				int wr = not.getIntrinsicWidth() * m, hr = not
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();

				// Fill a triangle
				int[] xpoints = { 1, wr - m, 1 };
				int[] ypoints = { 1, hr / 2, hr - 1 };
				Polygon triangle = new Polygon(xpoints, ypoints, 3);
				gc.setColor(bg_c);
				gc.fillPolygon(triangle);
				// Fill a small circle
				gc.fillOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				// Stroke out the boundary of the triangle
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.drawPolygon(triangle);
				// Stroke out the boundary of the small circle
				gc.drawOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				gc.dispose();
				not_img = buf;
			}
			img = not_img;
		}

		@Override
		public void visit(AndGate and) {
			if (and_img == null) {
				int wr = and.getIntrinsicWidth() * m, hr = and
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				Path2D.Float path = new Path2D.Float();
				path.moveTo(1, 1);
				path.lineTo(wr / 2, 1);
				path.curveTo(wr * 10 / 9, 1, wr * 10 / 9, hr - 1, wr / 2,
						hr - 1);
				path.lineTo(1, hr - 1);
				path.lineTo(1, 1);
				// Fill the path
				gc.setColor(bg_c);
				gc.fill(path);
				// Stroke out the path
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.draw(path);
				gc.dispose();
				and_img = buf;
			}
			img = and_img;
		}

		@Override
		public void visit(OrGate or) {
			if (or_img == null) {
				int wr = or.getIntrinsicWidth() * m, hr = or
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				Path2D.Float path = new Path2D.Float();
				path.moveTo(1, 1);
				path.quadTo(wr * 3 / 4, 1, wr - 1, hr / 2);
				path.quadTo(wr * 3 / 4, hr - 1, 1, hr - 1);
				path.quadTo(wr / 3, hr / 2, 1, 1);
				// Fill the path
				gc.setColor(bg_c);
				gc.fill(path);
				// Stroke out the path
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.draw(path);
				gc.dispose();
				or_img = buf;
			}
			img = or_img;
		}

		@Override
		public void visit(NandGate nand) {
			if (nand_img == null) {
				int wr = nand.getIntrinsicWidth() * m, hr = nand
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				Path2D.Float path = new Path2D.Float();
				path.moveTo(1, 1);
				path.lineTo(wr * 3 / 11, 1);
				path.curveTo(wr * 50 / 51, 1, wr * 50 / 51, hr - 1,
						wr * 3 / 11, hr - 1);
				path.lineTo(1, hr - 1);
				path.lineTo(1, 1);
				// Fill the path
				gc.setColor(bg_c);
				gc.fill(path);
				// Fill a small circle
				gc.fillOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				// Stroke out the path
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.draw(path);
				// Stroke out the boundary of the small circle
				gc.drawOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				gc.dispose();
				nand_img = buf;
			}
			img = nand_img;
		}

		@Override
		public void visit(NorGate nor) {
			if (nor_img == null) {
				int wr = nor.getIntrinsicWidth() * m, hr = nor
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				Path2D.Float path = new Path2D.Float();
				path.moveTo(1, 1);
				path.quadTo(wr * 3 / 5, 1, wr - m, hr / 2);
				path.quadTo(wr * 3 / 5, hr - 1, 1, hr - 1);
				path.quadTo(wr / 3, hr / 2, 1, 1);
				// Fill the path
				gc.setColor(bg_c);
				gc.fill(path);
				// Fill a small circle
				gc.fillOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				// Stroke out the path
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.draw(path);
				// Stroke out the boundary of the small circle
				gc.drawOval(wr - m + 1, hr / 2 - m / 2 + 1, m - 2, m - 2);
				gc.dispose();
				nor_img = buf;
			}
			img = nor_img;
		}

		@Override
		public void visit(XorGate xor) {
			if (xor_img == null) {
				int wr = xor.getIntrinsicWidth() * m, hr = xor
						.getIntrinsicHeight() * m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				Path2D.Float path = new Path2D.Float();
				path.moveTo(m, 1);
				path.quadTo(wr * 3 / 4, 1, wr - 1, hr / 2);
				path.quadTo(wr * 3 / 4, hr - 1, m, hr - 1);
				path.quadTo(wr / 3 + m, hr / 2, m, 1);
				// Fill the path
				gc.setColor(bg_c);
				gc.fill(path);
				// Stroke out the path
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.draw(path);
				// Stroke out a curve
				Path2D.Float curve = new Path2D.Float();
				curve.moveTo(1, 1);
				curve.quadTo(wr / 3, hr / 2, 1, hr - 1);
				gc.draw(curve);
				gc.dispose();
				xor_img = buf;
			}
			img = xor_img;
		}

		@Override
		public void visit(CGate c) {
			if (c_img == null) {
				int wr = c.getIntrinsicWidth() * m, hr = c.getIntrinsicHeight()
						* m;
				BufferedImage buf = new BufferedImage(wr, hr,
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D gc = buf.createGraphics();
				// Fill a rectangle shape
				gc.setColor(bg_c);
				gc.fillOval(0, 0, wr, hr);
				// Stroke out the boundary of the rectangle
				gc.setColor(br_c);
				gc.setStroke(new BasicStroke(2));
				gc.drawOval(0, 0, wr, hr);
				// Paint a character
				Font font = new Font("Courier", Font.BOLD, hr / 3);
				Rectangle2D bound = font.getStringBounds("C",
						gc.getFontRenderContext());
				gc.setFont(font);
				gc.drawString("C", Math.round(wr / 2 - bound.getWidth() / 2),
						Math.round(hr / 2 + bound.getHeight() / 3));
				gc.dispose();
				c_img = buf;
			}
			img = c_img;
		}

		@Override
		public void visit(Fanout fanout) {
			// TODO Auto-generated method stub

		}

	}
}
