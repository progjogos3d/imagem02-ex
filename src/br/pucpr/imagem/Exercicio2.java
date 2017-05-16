package br.pucpr.imagem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Exercício 2 - Aula 2
 * --------------------
 * Neste exercício, utilizamos a conversão do HSV para fazer algumas brincadeiras na imagem (aumentar o brilho,
 * a saturação e modificar o matiz).
 */
public class Exercicio2 {
    /** Caminho para a a pasta de imagens da aula. Modifique no Exercicio1. */
    private static final File PATH = Exercicio1.PATH;

    /**
     * Salva a imagem no disco.
     */
    private void salvar(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "jpg", new File(name + ".jpg"));
        System.out.printf("Salvo %s.png%n", name);
    }

    public float clamp(float value) {
        return value < 0 ? 0 : (value > 1 ? 1 : value);
    }

    public float rotate(float value) {
        while (value > 1) value -= 1;
        while (value < 0) value += 1;
        return value;
    }

    /**
     * Retorna o equivalente HSB da cor passada por parametro.
     */
    public float[] toHSB(int rgb) {
        Color c = new Color(rgb);
        return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    }

    /**
     * Altera o brilho da imagem de acordo com o percentual indicado em percent.
     */
    public BufferedImage bright(BufferedImage img, float percent) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

            }
        }
        return out;
    }

    /**
     * Altera a saturação da imagem de acordo com o percentual indicado em percent.
     */
    public BufferedImage saturate(BufferedImage img, float percent) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

            }
        }
        return out;
    }

    /**
     * Altera o hue da imagem de acordo com o valor passado por parametro.
     */
    public BufferedImage shiftHue(BufferedImage img, float amount) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

            }
        }
        return out;
    }

    /**
     * Executa o programa
     */
    public void run() throws IOException {
        //Carrega as imagens da tartaruga e do lagarto
        BufferedImage turtle = ImageIO.read(new File(PATH, "cor/turtle.jpg"));

    }

    public void main(String args[])  throws IOException {
        new Exercicio2().run();
    }

}
