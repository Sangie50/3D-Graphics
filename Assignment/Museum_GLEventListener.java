import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

/**
 * Event listener to Museum class.
 * 
 * @author    Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */

  
public class Museum_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Egg eggSet;
  private Spotlight spotlightRoot;
  private Phone phoneSet;
  private Room room;
  private Robot robotRoot;

  public Museum_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,8f,18f));
    this.camera.setTarget(new Vec3(0f,2f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light1.dispose(gl);
    light2.dispose(gl);

    eggSet.getEgg().dispose(gl);
    eggSet.getEggBase().dispose(gl);
    phoneSet.getPhone().dispose(gl);
    phoneSet.getPhoneBase().dispose(gl);
    room.getFloor().dispose(gl);
    room.getWall_1().dispose(gl);
    room.getWall_2_1().dispose(gl);
    room.getWall_2_2().dispose(gl);
    room.getWall_2_3().dispose(gl);
    room.getWall_2_4().dispose(gl);
    room.getDoor().dispose(gl);
    room.getOutsideWindow().dispose(gl);
    
  }

  // ***************************************************
  /* Animation
   *
   *
   */

  private boolean animation = false;
  private boolean moveRobot1To2 = false;
  private boolean moveRobot2To3 = false;
  private boolean moveRobot3To4 = false; 
  private boolean moveRobot4To5 = false; 
  private double savedTime = 0;
  private boolean turnLight1Off = true;
  private boolean turnLight2Off = true;
  private Shader lightOffShader;
  private Shader lightOnShader; 

  public void light1(){
    if (turnLight1Off){
      Material turnOff = new Material();
      turnOff.setAmbient(0f, 0f, 0f);
      turnOff.setDiffuse(0f, 0f, 0f);
      turnOff.setSpecular(0f, 0f, 0f);
      light1.setMaterial(turnOff);
      light1.setShader(lightOffShader);
      turnLight1Off = false;
    }
    else{
      Material turnOn = new Material();
      turnOn.setAmbient(0.3f, 0.3f, 0.3f);
      turnOn.setDiffuse(0.7f, 0.7f, 0.7f);
      turnOn.setSpecular(0.8f, 0.8f, 0.8f);
      light1.setMaterial(turnOn);
      light1.setShader(lightOnShader);
      turnLight1Off = true;
    }
  }

  public void light2(){
    if (turnLight2Off){
      Material turnOff = new Material();
      turnOff.setAmbient(0f, 0f, 0f);
      turnOff.setDiffuse(0f, 0f, 0f);
      turnOff.setSpecular(0f, 0f, 0f);
      light2.setMaterial(turnOff);
      light2.setShader(lightOffShader);
      turnLight2Off = false;
    }
    else{
      Material turnOn = new Material();
      turnOn.setAmbient(0.3f, 0.3f, 0.3f);
      turnOn.setDiffuse(0.7f, 0.7f, 0.7f);
      turnOn.setSpecular(0.8f, 0.8f, 0.8f);
      light2.setMaterial(turnOn);
      light2.setShader(lightOnShader);
      turnLight2Off = true;
    }
  }
   
  public void startAnimation(){
    animation = true;
    startTime = getSeconds()-savedTime;
    moveRobot1To2 = moveRobot2To3 = moveRobot3To4 = moveRobot4To5 = false;
  }
   
  
  public void stopAnimation(){
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }

  public void startMovingRobot1To2(){
    moveRobot1To2 = true;
    moveRobot2To3 = moveRobot3To4 = moveRobot4To5 = false;
    startTime = getSeconds()-savedTime;
    TransformNode robotRotate = robotRoot.getRobotRotate();
    robotRotate.setTransform(Mat4Transform.rotateAroundY(90));
    robotRotate.update();

    TransformNode leftEarTransform = robotRoot.getLeftEarTransform();
    Mat4 ear = Mat4Transform.scale(0.23f,2.6f,0.3f);
    ear = Mat4.multiply(ear, Mat4Transform.translate(-1.5f,0,0));
    leftEarTransform.setTransform(ear);
    leftEarTransform.update();
    TransformNode leftEarRotate = robotRoot.getRobotLeftEarRotate();
    leftEarRotate.setTransform(Mat4Transform.rotateAroundZ(10));    
    leftEarRotate.update();

    TransformNode rightEarRotate = robotRoot.getRobotRightEarRotate();
    rightEarRotate.setTransform(Mat4Transform.rotateAroundZ(-10));
    rightEarRotate.update();  
    TransformNode rightEarTransform = robotRoot.getRightEarTransform();
    ear = Mat4Transform.scale(0.23f,2.6f,0.3f);
    ear = Mat4.multiply(ear, Mat4Transform.translate(1.5f,0,0));
    rightEarTransform.setTransform(ear);
    rightEarTransform.update();
  }

  public void startMovingRobot2To3(){
    moveRobot2To3 = true;
    moveRobot1To2 = moveRobot3To4 = moveRobot4To5 = false;

    startTime = getSeconds()-savedTime;
    TransformNode robotMoveTranslate = robotRoot.getRobotMoveTranslate();
    robotMoveTranslate.setTransform(Mat4Transform.translate(1.96f , 0 , -6f)); 
    robotMoveTranslate.update();

    TransformNode robotRotate = robotRoot.getRobotRotate();
    robotRotate.setTransform(Mat4Transform.rotateAroundY(90));
    robotRotate.update();

    TransformNode bodyRotate = robotRoot.getBodyRotate();
    bodyRotate.setTransform(Mat4Transform.rotateAroundX(0));
    bodyRotate.update();

    TransformNode leftEar = robotRoot.getRobotLeftEarRotate();
    leftEar.setTransform(Mat4Transform.rotateAroundX(-19.20f));
    leftEar.update();
    TransformNode leftEarRotate = robotRoot.getRobotLeftEarRotate();
    leftEarRotate.setTransform(Mat4Transform.rotateAroundZ(32.4f));    
    leftEarRotate.update();

    TransformNode rightEarRotate = robotRoot.getRobotRightEarRotate();
    rightEarRotate.setTransform(Mat4Transform.rotateAroundZ(-32.4f));
    rightEarRotate.update();  
    TransformNode rightEar = robotRoot.getRobotRightEarRotate();
    rightEar.setTransform(Mat4Transform.rotateAroundX(19.13f));
    rightEar.update();      
  }

  public void startMovingRobot3To4(){
    moveRobot3To4 = true;
    moveRobot1To2 = moveRobot2To3 = moveRobot4To5 = false;
    startTime = getSeconds()-savedTime;

    TransformNode robotMoveTranslate = robotRoot.getRobotMoveTranslate();
    robotMoveTranslate.setTransform(Mat4Transform.translate(3.15f , 0 , -0.04f)); 
    robotMoveTranslate.update();

    TransformNode robotRotate = robotRoot.getRobotRotate();
    robotRotate.setTransform(Mat4Transform.rotateAroundY(90));
    robotRotate.update();

    TransformNode headRotate = robotRoot.getHeadRotate();
    headRotate.setTransform(Mat4Transform.rotateAroundX(20));
    headRotate.update();

    Mat4 noseShorter = Mat4Transform.scale(0.5f,0.3f,-2.19f);
    noseShorter = Mat4.multiply(noseShorter, Mat4Transform.translate(0,-3,-0.2f));
    TransformNode noseTransform = robotRoot.getNoseTransform();
    noseTransform.setTransform(noseShorter);
    noseTransform.update();
  }

  public void startMovingRobot4To5(){
    moveRobot4To5 = true;
    moveRobot1To2 = moveRobot2To3 = moveRobot3To4 = false;
    startTime = getSeconds()-savedTime;

    TransformNode robotMoveTranslate = robotRoot.getRobotMoveTranslate();
    robotMoveTranslate.setTransform(Mat4Transform.translate(1f , 0 , 6f)); 
    robotMoveTranslate.update();

    TransformNode robotRotate = robotRoot.getRobotRotate();
    robotRotate.setTransform(Mat4Transform.rotateAroundY(180));
    robotRotate.update();

    TransformNode bodyRotate = robotRoot.getBodyRotate();
    bodyRotate.setTransform(Mat4Transform.rotateAroundX(-5.96f));
    bodyRotate.update();

    TransformNode leftEarRotate = robotRoot.getRobotLeftEarRotate();
    leftEarRotate.setTransform(Mat4Transform.rotateAroundZ(32.4f));    
    leftEarRotate.update();

    TransformNode rightEar = robotRoot.getRobotRightEarRotate();
    rightEar.setTransform(Mat4Transform.rotateAroundZ(-32.4f));
    rightEar.update();    
    
    TransformNode leftEarTransform = robotRoot.getLeftEarTransform();
    Mat4 ear = Mat4Transform.scale(0.23f,3.49f,0.3f);
    ear = Mat4.multiply(ear, Mat4Transform.translate(-1.5f,0,0));
    leftEarTransform.setTransform(ear);
    leftEarTransform.update();

    TransformNode rightEarTransform = robotRoot.getRightEarTransform();
    ear = Mat4Transform.scale(0.23f,3.49f,0.3f);
    ear = Mat4.multiply(ear, Mat4Transform.translate(1.5f,0,0));
    rightEarTransform.setTransform(ear);
    rightEarTransform.update();
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model sphereRobot;
  private Light light1, light2, bulb;
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
    
    lightOffShader = new Shader(gl, "vs_light_01.txt", "fs_light_off.txt");
    lightOnShader = new Shader(gl, "vs_light_01.txt", "fs_light_01.txt");
    light1 = new Light(gl);
    light1.setCamera(camera);
    light1.setPosition(-6f,4f,10f);

    light2 = new Light(gl);
    light2.setCamera(camera);
    light2.setPosition(6.97f,3.5f,0.7f);
    
    bulb = new Light(gl);
    bulb.setCamera(camera);
    bulb.setPosition(4.3f,11.8f,0f);

    eggSet = new Egg(gl, camera, light1, light2, bulb);
    spotlightRoot = new Spotlight(gl, camera, light1, light2, bulb);
    phoneSet = new Phone(gl, camera, light1, light2, bulb);
    room = new Room(gl, camera, light1, light2, bulb);
    robotRoot = new Robot(gl, camera, light1, light2, bulb);  

  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //light2.setPosition(getLightPosition());  // changing light position each frame
    light1.render(gl);
    light2.render(gl);
    

    eggSet.getEgg().render(gl);
    eggSet.getEggBase().render(gl);
    spotlightRoot.getSpolight().draw(gl);
    phoneSet.getPhone().render(gl);
    phoneSet.getPhoneBase().render(gl);
    room.getFloor().render(gl);
    room.getWall_1().render(gl);
    room.getWall_2_1().render(gl);
    room.getWall_2_2().render(gl);
    room.getWall_2_3().render(gl);
    room.getWall_2_4().render(gl);
    room.getDoor().render(gl);
    room.getOutsideWindow().render(gl);
    robotRoot.getRobot().draw(gl);

    if (animation) spotlightRoot.swingSpotlight(startTime);
    if (moveRobot1To2) robotRoot.changeRobotPos1to2(startTime);
    if (moveRobot2To3) robotRoot.changeRobotPos2to3(startTime);
    if (moveRobot3To4) robotRoot.changeRobotPos3to4(startTime);
    if (moveRobot4To5) robotRoot.changeRobotPos4to5(startTime);
  }
  
  // The light's position is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 3.47f;
    float y = 11.5f;
    float z = 0.7f *(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }

  

  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}