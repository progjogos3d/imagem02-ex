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

    /** Constantes para o uso do HSB */
    private static final int H = 0;
    private static final int S = 1;
    private static final int B = 2;

    /**
     * Salva a imagem no disco.
     */
    private void salvar(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "jpg", new File(name + ".jpg"));
        System.out.printf("Salvo %s.png%n", name);
    }

    /**
     * Torna valores > 1 iguais 1. E valores < 0 iguais a 0.
     * Caso o valor esteja entre 0 e 1, fica inalterado.
     */
    private float clamp(float value) {
        return value < 0 ? 0 : (value > 1 ? 1 : value);
    }

    /**
     * Calcula o valor como se estivesse num "círculo" entre 0 e 1.
     * Por exemplo, o valor 2.8 teria dado "duas voltas" no círculo e seria 0.8.
     * O valor -2.4 seria o equivalente a 0.6.
     */
    private float rotate(float value) {
        while (value > 1) value -= 1;
        while (value < 0) value += 1;
        return value;
    }

    /**
     * Retorna o equivalente HSB da cor passada por parametro.
     */
    private float[] toHSB(int rgb) {
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
                //Lemos a cor do pixel em HSB (veja função toHSB acima)
                float[] p = toHSB(img.getRGB(x, y));

                //Ampliamos o brilho (a constrante B está definida no topo do arquivo)
                p[B] = clamp(p[B] * percent);

                //Convertemos a cor de volta para RGB
                int rgb = Color.HSBtoRGB(p[H], p[S], p[B]);

                out.setRGB(x, y, rgb);
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
                //Lemos a cor do pixel em HSB (veja função toHSB acima)
                float[] p = toHSB(img.getRGB(x, y));

                //Ampliamos o brilho (a constrante S está definida no topo do arquivo)
                p[S] = clamp(p[S] * percent);

                //Convertemos a cor de volta para RGB
                int rgb = Color.HSBtoRGB(p[H], p[S], p[B]);

                out.setRGB(x, y, rgb);
            }
        }
        return out;
    }

    /**
     * Altera o hue da imagem de acordo com a quatidade de graus (entre 0 e 360) passados por parametro.
     */
    public BufferedImage shiftHue(BufferedImage img, float degrees) {
        //Converte os graus para o intervalo de 0 a 1, usado pelo HSB
        float amount = degrees / 360.0f;
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Lemos a cor do pixel em HSB (veja função toHSB acima)
                float[] p = toHSB(img.getRGB(x, y));

                //Adicionamos um angulo ao componente H. Como o H é circular, usamos a função rotate para
                //ajustar esse parâmetro.
                p[H] = rotate(p[H] + amount);

                //Convertemos a cor de volta para RGB
                int rgb = Color.HSBtoRGB(p[H], p[S], p[B]);

                out.setRGB(x, y, rgb);
            }
        }
        return out;
    }

    /**
     * Executa o programa
     */
    public void run() throws IOException {
        //Carrega as imagens da tartaruga e do lagarto
        BufferedImage puppy = ImageIO.read(new File(PATH, "cor/puppy.jpg"));

        //Brilho
        salvar(bright(puppy, 2.0f), "puppyBrightMore");
        salvar(bright(puppy, 0.2f), "puppyBrightLess");

        //Saturação
        salvar(saturate(puppy, 1.5f), "puppySaturateMore");
        salvar(saturate(puppy, 0.5f), "puppySaturateLess");

        //Hue
        salvar(shiftHue(puppy, 30.0f), "puppyHue60");
        salvar(shiftHue(puppy, 120.0f), "puppyHue120");
        salvar(shiftHue(puppy, 180.0f), "puppyHue180");
        salvar(shiftHue(puppy, 240.0f), "puppyHue240");
        salvar(shiftHue(puppy, 300.0f), "puppyHue300");
    }

    public static void main(String args[])  throws IOException {
        new Exercicio2().run();
    }

}
