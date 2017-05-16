package br.pucpr.imagem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Exercicio 1 - Aula 2
 * --------------------
 * Este esse exercício propõe a construção de uma função chamada convolve, que realiza a aplicação de um kernel sobre
 * a imagem. O kernel é um array de 3x3 floats.
 *
 * Além disso, é necessário também criar funções para aplicação dos filtros de bordas, prewitt e laplace. Nesses
 * filtros, é necessário utilizar dois kernels diferentes e combiná-los com uma fórmula de distância. Este exemplo
 * implementa a função border, que aplica essa fórmula.
 *
 * Todos os kernels da aula são usados no exemplo, exatamente como o aluno deveria fazer. Altere no método main o
 * caminho da pasta de imagem.
 */
public class Exercicio1 {
    /** Caminho para a a pasta de imagens da aula */
    public static final File PATH = new File("/Users/vinigodoy/img");

    /**
     * Garante que o valor do pixel estará entre 0 e 255.
     */
    public int clamp(float value) {
        int v = (int)value;
        return v > 255 ? 255 : (v < 0 ? 0 : v);
    }

    /**
     * Converte os valores de r, g e b para o inteiro da cor.
     * Os valores podem estar fora do intervalo de 0 até 255, pois
     * a função ajusta chamando a função clamp (acima).
     */
    private int toColor(float r, float g, float b) {
        return new Color(clamp(r), clamp(g), clamp(b)).getRGB();
    }

    /**
     * Salva a imagem no disco.
     */
    private void salvar(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "jpg", new File(name + ".jpg"));
        System.out.printf("Salvo %s.png%n", name);
    }


    /**
     * Realiza a operação de convolução, isto é a aplicação do kernel sobre a imagem. Para cada pixel da imagem,
     * é calculada uma média ponterada entre ele e seus vizinhos. O kernel contém os pesos dessa média.
     */
    public BufferedImage convolve(BufferedImage img, float[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Valores de r, g e b finais
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Esses vizinhos estão descritos no kernel, por isso, fazemos um for para percorrer o kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtemos o pixel vizinho
                        Color pixel = new Color(img.getRGB(px, py));
                        //E somamos ele as cores finais multiplicadas pelo seu respectivo peso no kernel
                        r += pixel.getRed() * kernel[kx][ky];
                        g += pixel.getGreen() * kernel[kx][ky];
                        b += pixel.getBlue() * kernel[kx][ky];
                    }
                }

                //Calculamos a cor final
                out.setRGB(x, y, toColor(r, g, b));
            }
        }
        return out;
    }


    /**
     * Calculo da distancia entre dois gradientes. Usada nos filtros de borda.
     */
    public int distance(int tx, int ty) {
        return (int)Math.sqrt(tx * ty + ty * ty);
    }

    /**
     * Filtros de borda
     * ----------------
     * Calcula a distância dos tons de pixels nas duas imagens geradas com filtros de bordas.
     */
    public BufferedImage border(BufferedImage imgX, BufferedImage imgY) {
        //Para cada pixel, calcula a "distância" entre os dois gradientes.
        BufferedImage out = new BufferedImage(imgX.getWidth(), imgX.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imgX.getHeight(); y++) {
            for (int x = 0; x < imgX.getWidth(); x++) {
                //Obtém a cor das bordas em x e y
                Color px = new Color(imgX.getRGB(x, y));
                Color py = new Color(imgY.getRGB(x, y));

                //Calcula a distancia das cores em cada canal
                int dr = distance(px.getRed(), py.getRed());
                int dg = distance(px.getGreen(), py.getGreen());
                int db = distance(px.getBlue(), py.getBlue());

                //Salva o resultado na imagem de saída
                out.setRGB(x, y, toColor(dr, dg, db));
            }
        }
        return out;
    }

    /**
     * Executa os filtros.
     */
    public void run() throws IOException {
        //Carrega as imagens da tartaruga e do lagarto
        BufferedImage turtle = ImageIO.read(new File(PATH, "cor/turtle.jpg"));
        BufferedImage lizard = ImageIO.read(new File(PATH, "gray/lizard.jpg"));

        //Suavização
        //----------

        //Suavização pela média
        salvar(convolve(turtle, new float[][] {
                {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f},
                {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f},
                {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f}
        }), "turtleSuavizacaoMedia");

        //Suavização nítida
        salvar(convolve(turtle, new float[][] {
                {1.0f / 16.0f, 2.0f / 16.0f, 1.0f / 16.0f},
                {2.0f / 16.0f, 4.0f / 16.0f, 2.0f / 16.0f},
                {1.0f / 16.0f, 2.0f / 16.0f, 1.0f / 16.0f}
        }), "turtleSuavizacaoNitida");

        //Suavização em cruz (lembre-se que 0.2 = 1/5)
        salvar(convolve(turtle, new float[][] {
                {0.0f, 0.2f, 0.0f},
                {0.2f, 0.2f, 0.2f},
                {0.0f, 0.2f, 0.0f}
        }), "turtleSuavizacaoCruz");

        //Bordas
        //------

        //Laplace
        float[][] laplace = new float[][] {
            {0.0f,  1.0f, 0.0f},
            {1.0f, -4.0f, 1.0f},
            {0.0f,  1.0f, 0.0f}
        };

        salvar(convolve(turtle, laplace), "turtleLaplace");
        salvar(convolve(lizard, laplace), "lizardLaplace");

        float[][] laplaceDiagonais = new float[][] {
                {0.5f,  1.0f, 0.5f},
                {1.0f, -6.0f, 1.0f},
                {0.5f,  1.0f, 0.5f}
        };
        salvar(convolve(turtle, laplaceDiagonais), "turtleLaplaceDiagonais");
        salvar(convolve(lizard, laplaceDiagonais), "lizardLaplaceDiagonais");

        //Kernels de Sobel em Gx e Gy
        float[][] sobelGx = new float[][] {
                {-1.0f,  0.0f, 1.0f},
                {-1.0f,  0.0f, 1.0f},
                {-1.0f,  0.0f, 1.0f}
        };
        float[][] sobelGy = new float[][] {
                {-1.0f, -1.0f, -1.0f},
                { 0.0f,  0.0f,  0.0f},
                { 1.0f,  1.0f,  1.0f}
        };

        //Aplica Sobel com a tartaruga
        BufferedImage imgGX = convolve(turtle, sobelGx);
        BufferedImage imgGY = convolve(turtle, sobelGy);
        salvar(imgGX, "turtleSobelGx");
        salvar(imgGY, "turtleSobelGy");

        salvar(border(imgGX, imgGY), "turtleSobel");

        //Aplica Sobel com o lagarto
        imgGX = convolve(lizard, sobelGx);
        imgGY = convolve(lizard, sobelGy);
        salvar(imgGX, "lizardSobelGx");
        salvar(imgGY, "lizardSobelGy");
        salvar(border(imgGX, imgGY), "lizardSobel");

        //Kernels de Prewitt em Gx e Gy
        float[][] prewittGx = new float[][] {
                {-1.0f,  0.0f, 1.0f},
                {-2.0f,  0.0f, 2.0f},
                {-1.0f,  0.0f, 1.0f}
        };
        float[][] prewittGy = new float[][] {
                {-1.0f, -2.0f, -1.0f},
                { 0.0f,  0.0f,  0.0f},
                { 1.0f,  2.0f,  1.0f}
        };
        //Aplica Prewitt com a tartaruga
        imgGX = convolve(turtle, prewittGx);
        imgGY = convolve(turtle, prewittGy);
        salvar(imgGX, "turtlePrewittGx");
        salvar(imgGY, "turtlePrewittGy");

        salvar(border(imgGX, imgGY), "turtlePrewitt");

        //Aplica Prewitt com o lagarto
        imgGX = convolve(lizard, prewittGx);
        imgGY = convolve(lizard, prewittGy);
        salvar(imgGX, "lizardPrewittGx");
        salvar(imgGY, "lizardPrewittGy");
        salvar(border(imgGX, imgGY), "lizardPrewitt");

        //Sharpening
        salvar(convolve(turtle, new float[][] {
                { 0.0f, -1.0f,  0.0f},
                {-1.0f,  5.0f, -1.0f},
                { 0.0f, -1.0f,  0.0f}
        }), "turtleSharpening");

        //Emboss
        salvar(convolve(turtle, new float[][] {
                {-2.0f, -2.0f,  0.0f},
                {-2.0f,  6.0f,  0.0f},
                { 0.0f,  0.0f,  0.0f}
        }), "turtleEmboss1");

        salvar(convolve(turtle, new float[][] {
                { 0.0f,  0.0f,  0.0f},
                { 0.0f,  6.0f, -2.0f},
                { 0.0f, -2.0f, -2.0f}
        }), "turtleEmboss2");

        salvar(convolve(turtle, new float[][] {
                {-2.0f, -1.0f,  0.0f},
                {-1.0f,  1.0f,  1.0f},
                { 0.0f,  1.0f,  2.0f}
        }), "turtleEmboss3");
    }

    public static void main(String[] args) throws IOException {
        new Exercicio1().run();
    }
}
