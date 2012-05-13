/**
 * 
 */
package com.asys.constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
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
		int w = elt.getWidth() * m, h = elt.getHeight() * m;
		BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
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
		gc.drawImage(eif.getImage(elt), Constant.GATE_MARGIN,
				Constant.GATE_MARGIN, null);
		// gc.drawImage(eif.getImage(elt), Math.round(wr * r), Math.round(hr *
		// r),
		// Math.round(wr * (1 - 2 * r)), Math.round(hr * (1 - 2 * r)),
		// null);
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
			int wr = input.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = input
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Fill a rectangle shape
			gc.setColor(bg_c);
			gc.fillRect(2, 2, wr - 4, hr - 4);
			// Stroke out the boundary of the rectangle
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.drawRect(2, 2, wr - 4, hr - 4);
			// Paint a character
			Font font = new Font("Courier", Font.PLAIN, hr / 3);
			Rectangle2D bound = font.getStringBounds("I",
					gc.getFontRenderContext());
			gc.setFont(font);
			gc.drawString("I", Math.round(wr / 2 - bound.getWidth() / 2),
					Math.round(hr / 2 + bound.getHeight() / 3));
			gc.dispose();
			input_img = buf;
			img = input_img;
		}

		@Override
		public void visit(OutputGate output) {
			int wr = output.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = output
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Fill a rectangle shape
			gc.setColor(bg_c);
			gc.fillOval(2, 2, wr - 4, hr - 4);
			// Stroke out the boundary of the rectangle
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.drawOval(2, 2, wr - 4, hr - 4);
			// Paint a character
			Font font = new Font("Courier", Font.BOLD, hr / 3);
			Rectangle2D bound = font.getStringBounds("O",
					gc.getFontRenderContext());
			gc.setFont(font);
			gc.drawString("O", Math.round(wr / 2 - bound.getWidth() / 2),
					Math.round(hr / 2 + bound.getHeight() / 3));
			gc.dispose();
			output_img = buf;
			img = output_img;
		}

		@Override
		public void visit(EnvironmentGate env) {
			int wr = env.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = env
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Fill a rectangle shape
			gc.setColor(bg_c);
			gc.fillRect(2, 2, wr - 4, hr - 4);
			// Stroke out the boundary of the rectangle
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.drawRect(2, 2, wr - 4, hr - 4);
			// Paint a character
			Font font = new Font("Courier", Font.PLAIN, hr / 3);
			Rectangle2D bound = font.getStringBounds("E",
					gc.getFontRenderContext());
			gc.setFont(font);
			gc.drawString("E", Math.round(wr / 2 - bound.getWidth() / 2),
					Math.round(hr / 2 + bound.getHeight() / 3));
			gc.dispose();
			env_img = buf;
			img = env_img;
		}

		@Override
		public void visit(NotGate not) {
			int wr = not.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = not
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Fill a triangle
			int[] xpoints = { 2, wr - m, 2 };
			int[] ypoints = { 2, hr / 2, hr - 2 };
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
			img = not_img;
		}

		@Override
		public void visit(AndGate and) {
			int wr = and.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = and
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			Path2D.Float path = new Path2D.Float();
			path.moveTo(2, 2);
			path.lineTo(wr / 2, 2);
			path.curveTo(wr * 10 / 9, 2, wr * 10 / 9, hr - 2, wr / 2, hr - 2);
			path.lineTo(2, hr - 2);
			path.lineTo(2, 2);
			// Fill the path
			gc.setColor(bg_c);
			gc.fill(path);
			// Stroke out the path
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.draw(path);
			gc.dispose();
			and_img = buf;
			img = and_img;
		}

		@Override
		public void visit(OrGate or) {
			int wr = or.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = or
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			Path2D.Float path = new Path2D.Float();
			path.moveTo(2, 2);
			path.quadTo(wr * 3 / 4, 2, wr - 2, hr / 2);
			path.quadTo(wr * 3 / 4, hr - 2, 2, hr - 2);
			path.quadTo(wr / 3, hr / 2, 2, 2);
			// Fill the path
			gc.setColor(bg_c);
			gc.fill(path);
			// Stroke out the path
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.draw(path);
			gc.dispose();
			or_img = buf;
			img = or_img;
		}

		@Override
		public void visit(NandGate nand) {
			int wr = nand.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = nand
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			Path2D.Float path = new Path2D.Float();
			path.moveTo(2, 2);
			path.lineTo(wr * 3 / 11, 2);
			path.curveTo(wr * 47 / 51, 2, wr * 47 / 51, hr - 2, wr * 3 / 11,
					hr - 2);
			path.lineTo(2, hr - 2);
			path.lineTo(2, 2);
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
			img = nand_img;
		}

		@Override
		public void visit(NorGate nor) {
			int wr = nor.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = nor
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			Path2D.Float path = new Path2D.Float();
			path.moveTo(2, 2);
			path.quadTo(wr * 3 / 5, 2, wr - m, hr / 2);
			path.quadTo(wr * 3 / 5, hr - 2, 2, hr - 2);
			path.quadTo(wr / 3, hr / 2, 2, 2);
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
			img = nor_img;
		}

		@Override
		public void visit(XorGate xor) {
			int wr = xor.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = xor
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			Path2D.Float path = new Path2D.Float();
			path.moveTo(m * 2 / 3, 2);
			path.quadTo(wr * 3 / 4, 2, wr - 2, hr / 2);
			path.quadTo(wr * 3 / 4, hr - 2, m * 2 / 3, hr - 2);
			path.quadTo(wr * 7 / 24 + m * 2 / 3, hr / 2, m * 2 / 3, 2);
			// Fill the path
			gc.setColor(bg_c);
			gc.fill(path);
			// Stroke out the path
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.draw(path);
			// Stroke out a curve
			Path2D.Float curve = new Path2D.Float();
			curve.moveTo(2, 2);
			curve.quadTo(wr / 3, hr / 2, 2, hr - 2);
			gc.draw(curve);
			gc.dispose();
			xor_img = buf;
			img = xor_img;
		}

		@Override
		public void visit(CGate c) {
			int wr = c.getIntrinsicWidth() * m - 2 * Constant.GATE_MARGIN, hr = c
					.getIntrinsicHeight() * m - 2 * Constant.GATE_MARGIN;
			BufferedImage buf = new BufferedImage(wr, hr,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D gc = buf.createGraphics();
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Fill a rectangle shape
			gc.setColor(bg_c);
			gc.fillOval(2, 2, wr - 4, hr - 4);
			// Stroke out the boundary of the rectangle
			gc.setColor(br_c);
			gc.setStroke(new BasicStroke(2));
			gc.drawOval(2, 2, wr - 4, hr - 4);
			// Paint a character
			Font font = new Font("Courier", Font.BOLD, hr / 3);
			Rectangle2D bound = font.getStringBounds("C",
					gc.getFontRenderContext());
			gc.setFont(font);
			gc.drawString("C", Math.round(wr / 2 - bound.getWidth() / 2),
					Math.round(hr / 2 + bound.getHeight() / 3));
			gc.dispose();
			c_img = buf;
			img = c_img;
		}

		@Override
		public void visit(Fanout fanout) {
			// TODO Auto-generated method stub

		}

	}
}
