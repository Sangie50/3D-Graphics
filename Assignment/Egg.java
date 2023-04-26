import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;

/**
 * Builds egg and its base.
 * 
 * @author  Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */



public class Egg{
  public Model eggBase;
  public Model egg;

  public Egg(GL3 gl, Camera camera, Light light1, Light light2, Light bulb){
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/base.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/mar0kuu2.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/mar0kuu2_specular.jpg");

    //Egg base
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4f,0.7f,4f), Mat4Transform.translate(0,0.5f,0));
    eggBase = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);
    
    //egg
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.8f, 0.8f, 0.8f), new Vec3(0.8f, 0.8f, 0.8f), 5.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(3.5f,6f,3.5f), Mat4Transform.translate(0,0.6f,0));
    egg = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId2, textureId1);
  }

  public Model getEgg(){
    return egg;
  }
  public Model getEggBase(){
    return eggBase;
  }
}
  