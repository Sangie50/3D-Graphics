import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;

/**
 * Builds phone and its base.
 * 
 * @author    Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */


public class Phone{
  public Model phoneBase;
  public Model phone;
  
  public Phone(GL3 gl, Camera camera, Light light1, Light light2, Light bulb){
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/base.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/phone.jpg");
    //Phone base
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4f,0.7f,3.5f), Mat4Transform.translate(1.5f,0.5f,-2.3f));
    phoneBase = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);
    
    //Phone
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2.1f,4.7f,0.3f), Mat4Transform.translate(2.9f,0.5f,-27.3f));
    phone = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId1);
  }

  public Model getPhone(){
    return phone;
  }
  public Model getPhoneBase(){
    return phoneBase;
  }

}