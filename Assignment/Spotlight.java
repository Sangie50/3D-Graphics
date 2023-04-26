import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;


/**
 * Builds spotlight as a hierarchical model that can swing its bulb around the x-axis.
 * 
 * @author  Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */

public class Spotlight{
  public NameNode spotlight;
  private Model cube, sphere;
  private TransformNode spotlightBulbHolderRotate;
  private double savedTime = 0;

  public Spotlight(GL3 gl, Camera camera, Light light1, Light light2, Light bulb){
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/spotlight.jpg");

    //spotlight cubes
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0,0));
    cube = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId0);
    
    //spotlight sphere
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_light_01.txt", "fs_light_01.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.8f, 0.8f, 0.8f), new Vec3(0.8f, 0.8f, 0.8f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0,0));
    sphere = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh);

    //spotlight base
    float spotlightBaseHeight = 0.5f;
    spotlight = new NameNode("spotlight structure");

    NameNode spotlightBase = new NameNode("spotlight base");
    Mat4 m = Mat4Transform.scale(1,spotlightBaseHeight,1);
    m = Mat4.multiply(m, Mat4Transform.translate(7.3f,spotlightBaseHeight,0));
    TransformNode spotlightBaseTransform = new TransformNode("scale(1,"+spotlightBaseHeight+",1); translate(7.3,"+spotlightBaseHeight+",0)", m);
    ModelNode spotlightBaseShape = new ModelNode("Cube(0)", cube);
    TransformNode translateToBase = new TransformNode("translate(7.3,"+spotlightBaseHeight+",0)",Mat4Transform.translate(7.3f,spotlightBaseHeight,0));
    
    //spotlight stick
    float spotlightStickHeight = 12f;
    NameNode spotlightStick = new NameNode("spotlight stick");
    m = Mat4Transform.scale(0.25f,spotlightStickHeight,0.25f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode spotlightStickTransform = new TransformNode("scale(0.25f,"+spotlightStickHeight+",0.25f);translate(0,0.5,0)", m);
    ModelNode spotlightStickShape = new ModelNode("Cube(1)", cube);
    TransformNode translateToStick = new TransformNode("translate(0,"+spotlightStickHeight+",0)",Mat4Transform.translate(0,spotlightStickHeight,0));
    
    //spotlight hanger
    //with scene graphs, we only need to think about the immediate parent, not about any of the parent's ancestors
    float spotlightHangerLength = 3f;
    float spotlighthangerHeight = 0.25f;
    NameNode spotlightHanger = new NameNode("spotlight hanger");
    m = Mat4Transform.scale(spotlightHangerLength,spotlighthangerHeight,0.25f); 
    m = Mat4.multiply(m, Mat4Transform.translate(-0.46f,0,0));
    TransformNode spotlightHangerTransform = new TransformNode("scale(4,"+spotlighthangerHeight+"0.25,0.25); translate(-0.47,0,0)", m);
    ModelNode spotlightHangerShape = new ModelNode("Cube(2)", cube);
    TransformNode translateToHanger = new TransformNode("translate(-0.47,"+spotlighthangerHeight+",0)",Mat4Transform.translate(-0.47f,-spotlighthangerHeight,0));
    
    //spotlight bulb holder
    float spotlightBulbHolderHeight = 1f;
    NameNode spotlightBulbHolder = new NameNode("spotlight bulb holder");
    m = Mat4Transform.scale(0.7f,spotlightBulbHolderHeight,0.7f); 
    m = Mat4.multiply(m, Mat4Transform.translate(-3.47f,0,0));
    spotlightBulbHolderRotate = new TransformNode("spotlight bulb holder rotate",Mat4Transform.rotateAroundX(180));
    TransformNode spotlightBulbHolderTransform = new TransformNode("scale(0.7,"+spotlightBulbHolderHeight+",0.7); translate(-3.47,0,0)", m);
    ModelNode spotlightBulbHolderShape = new ModelNode("Cube(3)", cube);
    TransformNode translateToBulbHolder = new TransformNode("translate(0,"+spotlightBulbHolderHeight+"+0.8,0)",Mat4Transform.translate(0,spotlightBulbHolderHeight+0.8f,0));
    
    //spotlight bulb
    float spotlightBulbScale = 0.9f;
    NameNode spotlightBulb = new NameNode("spotlight bulb"); 
    //m = new Mat4(1);
    //m = Mat4.multiply(m, Mat4Transform.translate(0,bodyHeight,0));
    m = Mat4.multiply(m, Mat4Transform.scale(spotlightBulbScale,spotlightBulbScale,spotlightBulbScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,-1.6f,0));
    TransformNode spotlightBulbTransform = new TransformNode("scale(0.7,"+spotlightBulbScale+",0.7); translate(0,-1.5,0)", m);
    ModelNode spotlightBulbShape = new ModelNode("Sphere(head)", sphere);
    TransformNode translateToBulb = new TransformNode("translate(0,"+spotlightBulbScale+",0)",Mat4Transform.translate(0,spotlightBulbScale,0));
    

    spotlight.addChild(spotlightBase);
      spotlightBase.addChild(spotlightBaseTransform);
        spotlightBaseTransform.addChild(spotlightBaseShape);
        spotlightBase.addChild(translateToBase);
        
        translateToBase.addChild(spotlightStick);
          spotlightStick.addChild(spotlightStickTransform);
            spotlightStickTransform.addChild(spotlightStickShape);
            spotlightStick.addChild(translateToStick);
            
            translateToStick.addChild(spotlightHanger);
              spotlightHanger.addChild(spotlightHangerTransform);
                spotlightHangerTransform.addChild(spotlightHangerShape);
                  spotlightHanger.addChild(translateToHanger);
              
              translateToHanger.addChild(spotlightBulbHolder);
                spotlightBulbHolder.addChild(spotlightBulbHolderRotate);
                  spotlightBulbHolderRotate.addChild(spotlightBulbHolderTransform);
                  spotlightBulbHolderTransform.addChild(spotlightBulbHolderShape);
                    spotlightBulbHolderRotate.addChild(translateToBulbHolder);

                translateToBulbHolder.addChild(spotlightBulb);
                  spotlightBulb.addChild(spotlightBulbTransform);
                    spotlightBulbTransform.addChild(spotlightBulbShape);
                      spotlightBulb.addChild(translateToBulb);


    spotlight.update(); // IMPORTANT – must be done every time any part of the scene graph changes
    // Following two lines can be used to check scene graph construction is correct
    //spotlight.print(0, false);
    //System.exit(0);
  }

  public NameNode getSpolight(){
    return spotlight;
  }

  public TransformNode getSpotlightRotate(){
    return spotlightBulbHolderRotate;
  }

  public void swingSpotlight(Double startTime){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = -180f-30f*(float)Math.sin(elapsedTime);
    spotlightBulbHolderRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    spotlightBulbHolderRotate.update();
  }

  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  


}