import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;
import java.time.LocalTime;

/**
 * Builds a room with 2 walls, a floor and a window.
 * 
 * @author  Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */



public class Room{
  public Model floor, wall_1, wall_2_1, wall_2_2, wall_2_3, wall_2_4, door, outsideWindow;
  private int[] currentOutsideTexture;
  private final LocalTime MORNING = LocalTime.of(4,0,0);
  private final LocalTime NIGHT = LocalTime.of(18,0,0);

  public Room(GL3 gl, Camera camera, Light light1, Light light2, Light bulb){
    float size = 20f;
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/floor.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/door.jpg");

    //To change outside window texted based on the time of day
   
    LocalTime timeNow = LocalTime.now();
    if (timeNow.isAfter(MORNING) && timeNow.isBefore(NIGHT)){
      currentOutsideTexture = TextureLibrary.loadTexture(gl, "textures/outside-day.jpg");
    }
    else{
      currentOutsideTexture = TextureLibrary.loadTexture(gl, "textures/outside-night.jpg");
    }

    //floor
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(size,0f,size);
    floor = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId1);


    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), Mat4Transform.scale(size,1f,size));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
    wall_1 = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);

    //door on wall_1
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.0f, 0f, 0f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 120.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), Mat4Transform.scale(size/2.5f,0.5f,size/2f));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size/4.2f,size/4,-size/2.1f), modelMatrix);
    door = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId3);


    //wall_2 with window
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), Mat4Transform.scale(size/3,1f,size));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*-0.5f,size*0.5f,size/3), modelMatrix);
    wall_2_1 = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), Mat4Transform.scale(size/3,1f,size));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*-0.5f,size*0.5f,-size/3), modelMatrix);
    wall_2_2 = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), Mat4Transform.scale(size/3,1f,size/3));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*-0.5f,(size/3)*0.5f,0), modelMatrix);
    wall_2_3 = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);

    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), Mat4Transform.scale(size/3,1f,size/3));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*-0.5f,size*0.833f,0), modelMatrix);
    wall_2_4 = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);

    //outside scenario
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.05f, 0.05f, 0.05f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 120.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), Mat4Transform.scale(size*1.7f,1f,size*1.2f));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.66f,size*0.5f,0), modelMatrix);
    outsideWindow = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, currentOutsideTexture);
  }

  public Model getFloor(){
    return floor;
  }

  public Model getWall_1(){
    return wall_1;
  }

  public Model getWall_2_1(){
    return wall_2_1;
  }

  public Model getWall_2_2(){
    return wall_2_2;
  }

  public Model getWall_2_3(){
    return wall_2_3;
  }

  public Model getWall_2_4(){
    return wall_2_4;
  }

  public Model getDoor(){
    return door;
  }

  public Model getOutsideWindow(){
    return outsideWindow;
  }
}
  