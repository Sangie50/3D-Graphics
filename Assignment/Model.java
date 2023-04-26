import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Model {
  
  private Mesh mesh;
  private int[] textureId1; 
  private int[] textureId2; 
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private Light light1;
  private Light light2;
  private Light bulb;
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Light bulb, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.bulb = bulb;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Light bulb, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1) {
    this(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Light bulb, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setLight1(Light light1) {
    this.light1 = light1;
  }

  public void setLight2(Light light2) {
    this.light2 = light2;
  }

  public void setBulb(Light bulb) {
    this.bulb = bulb;
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());
    
    //new
    //light 1
    shader.setVec3(gl, "light1.position", light1.getPosition());
    shader.setVec3(gl, "light1.ambient", light1.getMaterial().getAmbient());
    shader.setVec3(gl, "light1.diffuse", light1.getMaterial().getDiffuse());
    shader.setVec3(gl, "light1.specular", light1.getMaterial().getSpecular());

    //light 2
    shader.setVec3(gl, "light2.position", light2.getPosition());
    shader.setVec3(gl, "light2.ambient", light2.getMaterial().getAmbient());
    shader.setVec3(gl, "light2.diffuse", light2.getMaterial().getDiffuse());
    shader.setVec3(gl, "light2.specular", light2.getMaterial().getSpecular());

    //bulb
    shader.setVec3(gl, "bulb.position", bulb.getPosition());
    shader.setVec3(gl, "bulb.ambient", bulb.getMaterial().getAmbient());
    shader.setVec3(gl, "bulb.diffuse", bulb.getMaterial().getDiffuse());
    shader.setVec3(gl, "bulb.specular", bulb.getMaterial().getSpecular());

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());  

    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
    }
    mesh.render(gl);
  } 
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
    if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  }
  
}