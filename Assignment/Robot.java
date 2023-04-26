import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;

/**
 * Builds an animated robot as a a hierarchical model. 
 * 
 * @author   Sangamithra Bala Amuthan <sbalaamuthan1@sheffield.ac.uk>
 */


public class Robot{
  public NameNode robot;
  private Model sphereRobot;
  private TransformNode robotMoveTranslate, robotRotate, footRotate, bodyRotate, headRotate, neckRotate, leftEarRotate, leftEarTransform, rightEarTransform, rightEarRotate, noseTransform;
  private Mat4 noseLonger;
  private float xPosition = -5f;
  private float zPosition = -6f;
  private static float pose_2Z = -0.037f;
  private static float pose_2X = 3.15f;
  private static float pose_3Z = 6f;
  private static float pose_3X = 1f;
  private float eggTurnLeftAngle = 0;
  private float earHeight = 2.6f;
  private float resetEarHeight = 3.49f;

  private Museum_GLEventListener glEventListener;
  
  public Robot(GL3 gl, Camera camera, Light light1, Light light2, Light bulb){
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/robot.jpg");
    glEventListener = new Museum_GLEventListener(camera);

    //sphere robot
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    Material material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.8f, 0.8f, 0.8f), new Vec3(0.8f, 0.8f, 0.8f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0,0));
    sphereRobot = new Model(gl, camera, light1, light2, bulb, shader, material, modelMatrix, mesh, textureId1);
    
    // robot 
    float footScale = 0.9f;
    float bodyHeight = 3f;
    float bodyWidth = 1.5f;
    float bodyDepth = 2f;
    float headScale = 1.2f;
    float neckLength = 2f;
    float neckHeight = 0.3f;    
    float earLength = 0.23f;
    float earDepth = 0.2f;
    float noseLength = 0.5f;
    float noseHeight = 0.3f;
    

    robot = new NameNode("robot");
    robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,0f,zPosition));
    robotRotate = new TransformNode("robot rotate",Mat4Transform.rotateAroundY(0));
    TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0.5f,0));

    //foot
    NameNode robotFoot = new NameNode("robot foot");
    Mat4 robotMatrix = Mat4Transform.scale(footScale,footScale,footScale);
    robotMatrix = Mat4.multiply(robotMatrix, Mat4Transform.translate(0f,0f,0f));
    footRotate = new TransformNode("foot rotate",Mat4Transform.rotateAroundX(90));
    TransformNode footTransform = new TransformNode("scale(0.9,0.9,0.9); translate(0,0.55,0)", robotMatrix);
    ModelNode footShape = new ModelNode("SphereRobot(0)", sphereRobot);
    TransformNode translateToFoot = new TransformNode("translate(-4.5,0.2,-5.5)",Mat4Transform.translate(0f,0.2f,0f));
    
    //body
    NameNode robotBody = new NameNode("robot body");
    robotMatrix = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
    robotMatrix = Mat4.multiply(robotMatrix, Mat4Transform.translate(0,0.5f,0)); 
    bodyRotate = new TransformNode("body rotate",Mat4Transform.rotateAroundX(0));
    TransformNode bodyTransform = new TransformNode("scale(1.5,3,2); translate(0,0.5,0)", robotMatrix);
    ModelNode bodyShape = new ModelNode("SphereRobot(1)", sphereRobot);
    TransformNode translateToBody = new TransformNode("translate(0,"+bodyHeight+",0)",Mat4Transform.translate(0,bodyHeight,0));
    
    //neck
    NameNode robotNeck = new NameNode("robot neck");
    robotMatrix = Mat4Transform.scale(neckLength,neckHeight,1f);
    neckRotate = new TransformNode("neck rotate",Mat4Transform.rotateAroundY(0));
    TransformNode neckTransform = new TransformNode("scale(2,0.3,1)", robotMatrix);
    ModelNode neckShape = new ModelNode("SphereRobot(2)", sphereRobot);
    TransformNode translateToNeck = new TransformNode("translate(0,"+neckHeight+",0)",Mat4Transform.translate(0,neckHeight,0));
    
    //head
    NameNode robotHead = new NameNode("robot head");
    robotMatrix = Mat4Transform.scale(headScale,headScale+0.2f,headScale);
    robotMatrix = Mat4.multiply(robotMatrix, Mat4Transform.translate(0,0.2f,0));
    headRotate = new TransformNode("head rotate",Mat4Transform.rotateAroundX(0));
    TransformNode headTransform = new TransformNode("scale(0.75,0.95,0.75); translate(0,0.2,0)", robotMatrix);
    ModelNode headShape = new ModelNode("SphereRobot(3)", sphereRobot);
    TransformNode translateToHead = new TransformNode("translate(0,"+headScale+",0)",Mat4Transform.translate(0,headScale,0));
    
    //left ear
    NameNode robotLeftEar = new NameNode("robot left ear");
    robotMatrix = Mat4Transform.scale(earLength,earHeight,earDepth);
    robotMatrix = Mat4.multiply(robotMatrix, Mat4Transform.translate(-1.5f,0,0));
    leftEarRotate = new TransformNode("left ear rotate", Mat4Transform.rotateAroundZ(10));
    leftEarTransform = new TransformNode("scale(0.25,2.3,0.2); translate(-1.5,0.5,0)", robotMatrix);
    ModelNode leftEarShape = new ModelNode("SphereRobot(4)", sphereRobot);
    
    //right ear
    NameNode robotRightEar = new NameNode("robot right ear");
    robotMatrix = Mat4Transform.scale(earLength,earHeight,earDepth);
    robotMatrix = Mat4.multiply(robotMatrix, Mat4Transform.translate(1.5f,0,0));
    rightEarRotate = new TransformNode("right ear rotate", Mat4Transform.rotateAroundZ(-10));
    rightEarTransform = new TransformNode("scale(0.25,2.3,0.2); translate(1.5,0,0)", robotMatrix);
    ModelNode rightEarShape = new ModelNode("SphereRobot(5)", sphereRobot);
    
    //nose
    NameNode robotNose = new NameNode("robot nose");
    noseLonger = Mat4Transform.scale(noseLength,noseHeight,0.2f);
    noseLonger = Mat4.multiply(noseLonger, Mat4Transform.translate(0,-3,3f));
    noseTransform = new TransformNode("scale(0.5,0.3,0.2); translate(0,-3,1)", noseLonger);
    ModelNode noseShape = new ModelNode("SphereRobot(6)", sphereRobot);
    
    robot.addChild(robotMoveTranslate);
    robotMoveTranslate.addChild(robotTranslate);
    robotTranslate.addChild(robotRotate);
    robotRotate.addChild(robotFoot);
      robotFoot.addChild(footRotate);
      footRotate.addChild(footTransform);
        footTransform.addChild(footShape);
        robotFoot.addChild(translateToFoot);

        translateToFoot.addChild(bodyRotate);
        bodyRotate.addChild(robotBody);
          robotBody.addChild(bodyTransform);
            bodyTransform.addChild(bodyShape);
            robotBody.addChild(translateToBody);
          
            translateToBody.addChild(neckRotate);
              neckRotate.addChild(robotNeck);
              robotNeck.addChild(neckTransform);
                neckTransform.addChild(neckShape);
                robotNeck.addChild(translateToNeck);

                translateToNeck.addChild(headRotate);
                  headRotate.addChild(robotHead);
                   robotHead.addChild(headTransform);
                      headTransform.addChild(headShape);
                      robotHead.addChild(translateToHead);

                translateToHead.addChild(robotLeftEar);
                  robotLeftEar.addChild(leftEarRotate);
                    leftEarRotate.addChild(leftEarTransform);
                      leftEarTransform.addChild(leftEarShape);
                
                translateToHead.addChild(robotRightEar);
                  robotRightEar.addChild(rightEarRotate);
                    rightEarRotate.addChild(rightEarTransform);
                      rightEarTransform.addChild(rightEarShape);
                    
                translateToHead.addChild(robotNose);
                  robotNose.addChild(noseTransform);
                    noseTransform.addChild(noseShape);
          
    robot.update();  // IMPORTANT - don't forget this
    //robot.print(0, false);
    //System.exit(0);

  }

  public NameNode getRobot(){
    return robot;
  }

  public TransformNode getRobotRotate(){
    return robotRotate;
  }

  public TransformNode getRobotMoveTranslate(){
    return robotMoveTranslate;
  }

  public TransformNode getRobotLeftEarRotate(){
    return leftEarRotate;
  }

  public TransformNode getRobotRightEarRotate(){
    return rightEarRotate;
  }

  public TransformNode getHeadRotate(){
    return headRotate;
  }

  public TransformNode getBodyRotate(){
    return bodyRotate;
  }

  public TransformNode getNoseTransform(){
    return noseTransform;
  }

  public TransformNode getLeftEarTransform(){
    return leftEarTransform;
  }

  public TransformNode getRightEarTransform(){
    return rightEarTransform;
  }



  public void changeRobotPos1to2(Double startTime){
    double elapsedTime = getSeconds() - startTime;
    double sum = 0;
    if (elapsedTime < 3.5) {
      robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition + (float)elapsedTime*2f , 0 , zPosition)); 
      robotMoveTranslate.update();
      footRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*30f));
      footRotate.update();
    }
    else if (elapsedTime < 12){
      bodyRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*1.2f));
      bodyRotate.update();
      leftEarRotate.setTransform(Mat4Transform.rotateAroundX(-(float)elapsedTime*1.6f));
      leftEarRotate.update();
      rightEarRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*1.6f));
      rightEarRotate.update();
    }
  }
  
  public void changeRobotPos2to3(Double startTime){
    //positions are adjusted so they start from the previous position after time lapse
    double elapsedTime = getSeconds() - startTime;
    float resettingBodyAngle = 14.37f-(float)elapsedTime*3f;
    float resettingLeftEarAngle = -19.20f+(float)elapsedTime*1.6f;
    float resettingRightEarAngle = 19.13f-(float)elapsedTime*1.6f;
    float headForwardAngle = (float)elapsedTime*5f - 150f;
    float resettingRobotAngle = 270f - (float)elapsedTime*15f; 
    float movingToSpolight_Z = zPosition + (float)elapsedTime - 18.01f;
    float movingToSpolight_X = 1.96f + (float)elapsedTime*0.2f - 3.6f;
    float turnBodyToSpotlight = (float)elapsedTime*15f - 360f;
    float noseTaller = 0.2f + (float)elapsedTime*0.3f - 8.2f;

    if (resettingBodyAngle >= 0) {
      bodyRotate.setTransform(Mat4Transform.rotateAroundX(resettingBodyAngle));
      bodyRotate.update();
    }
    else if (resettingLeftEarAngle <= 0 || resettingRightEarAngle >= 0){
      leftEarRotate.setTransform(Mat4Transform.rotateAroundX(-19.20f+(float)elapsedTime*1.6f));
      leftEarRotate.update();
      rightEarRotate.setTransform(Mat4Transform.rotateAroundX(19.13f-(float)elapsedTime*1.6f));
      rightEarRotate.update();
    }
    else if (resettingRobotAngle >= 0){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(resettingRobotAngle));
      robotRotate.update();
    }
    else if (elapsedTime < 24f) {
      robotMoveTranslate.setTransform(Mat4Transform.translate(movingToSpolight_X, 0.3f , movingToSpolight_Z)); 
      robotMoveTranslate.update();
      footRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*30f));
      footRotate.update();
    }
    else if (turnBodyToSpotlight <= 90){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(turnBodyToSpotlight));
      robotRotate.update();
    }
    else if (elapsedTime <= 34){
      noseLonger = Mat4Transform.scale(0.5f,0.3f,noseTaller);
      noseLonger = Mat4.multiply(noseLonger, Mat4Transform.translate(0,-3,0.2f));
      noseTransform.setTransform(noseLonger);
      noseTransform.update();

      if (headForwardAngle <= 20) {
        headRotate.setTransform(Mat4Transform.rotateAroundX(headForwardAngle));
        headRotate.update();
      }
    }
  }

  public void changeRobotPos3to4(Double startTime){
    //positions are adjusted so they start from the previous position after time lapse of previous animations
    double elapsedTime = getSeconds() - startTime;
    float resettingHeadAngle = 20f-(float)elapsedTime*10f;
    float noseShorter = 2.19f - (float)elapsedTime*0.5f;
    float resettingRobotAngle = 90 - ((float)elapsedTime-4.5f)*20f; //20 is rotating speed
    
    
    //resetting from pose 2
    if (elapsedTime < 4.5){
      if (noseShorter >= 0.2){
        noseLonger = Mat4Transform.scale(0.5f,0.3f, noseShorter);
        noseLonger = Mat4.multiply(noseLonger, Mat4Transform.translate(0,-3,0.6f/noseShorter));
        noseTransform.setTransform(noseLonger);
        noseTransform.update();
        
      }
      if (resettingHeadAngle >= 0) {
        headRotate.setTransform(Mat4Transform.rotateAroundX(resettingHeadAngle));
        headRotate.update();
      }
    }
    else if (resettingRobotAngle >=0){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(resettingRobotAngle));
      robotRotate.update();
    }
    else if(pose_2Z <= 6){
      robotMoveTranslate.setTransform(Mat4Transform.translate(3.15f, 0.3f , pose_2Z)); 
      robotMoveTranslate.update();
      footRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*70f));
      footRotate.update();
      pose_2Z += 0.1f;
    }
    else if (eggTurnLeftAngle >= -90){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(eggTurnLeftAngle));
      robotRotate.update();
      eggTurnLeftAngle -= 10;
      
    }
    else if(pose_2X >= 1){
      robotMoveTranslate.setTransform(Mat4Transform.translate(pose_2X, 0.3f , pose_2Z)); 
      robotMoveTranslate.update();
      footRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*60f));
      footRotate.update();
      pose_2X -= 0.05f;
    }
    else if (eggTurnLeftAngle >= -180){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(eggTurnLeftAngle));
      robotRotate.update();
      eggTurnLeftAngle -= 10;
    }
    else if(elapsedTime < 13){
      leftEarRotate.setTransform(Mat4Transform.rotateAroundZ((float)elapsedTime*2.5f));
      leftEarRotate.update();

      rightEarRotate.setTransform(Mat4Transform.rotateAroundZ(-(float)elapsedTime*2.5f));
      rightEarRotate.update();

      Mat4 ear = Mat4Transform.scale(0.23f,earHeight,0.3f);
      ear = Mat4.multiply(ear, Mat4Transform.translate(-1.5f,0,0));
      leftEarTransform.setTransform(ear);
      leftEarTransform.update();

      ear = Mat4Transform.scale(0.23f,earHeight,0.3f);
      ear = Mat4.multiply(ear, Mat4Transform.translate(1.5f,0,0));
      rightEarTransform.setTransform(ear);
      rightEarTransform.update();
      earHeight += 0.01;

      bodyRotate.setTransform(Mat4Transform.rotateAroundX(-(float)elapsedTime*3f + 33f));
      bodyRotate.update();
    }
  }

  public void changeRobotPos4to5(Double startTime){
    //positions are adjusted so they start from the previous position after time lapse of previous animations
    double elapsedTime = getSeconds() - startTime;
    float turingRobotAngle = -180f + (float)elapsedTime*9f;
    float neckAngle = (float)elapsedTime*3f -24f;
    if (earHeight > 2.6){
      Mat4 ear = Mat4Transform.scale(0.23f,earHeight,0.3f);
      ear = Mat4.multiply(ear, Mat4Transform.translate(-1.5f,0,0));
      leftEarTransform.setTransform(ear);
      leftEarTransform.update();

      ear = Mat4Transform.scale(0.23f,earHeight,0.3f);
      ear = Mat4.multiply(ear, Mat4Transform.translate(1.5f,0,0));
      rightEarTransform.setTransform(ear);
      rightEarTransform.update();
      earHeight -= 0.01;
    }
    else if (turingRobotAngle <= -135){
      robotRotate.setTransform(Mat4Transform.rotateAroundY(turingRobotAngle));
      robotRotate.update();
    }
    else if (elapsedTime < 8) {
      robotMoveTranslate.setTransform(Mat4Transform.translate(pose_3X, 0.3f , pose_3Z)); 
      robotMoveTranslate.update();
      footRotate.setTransform(Mat4Transform.rotateAroundX((float)elapsedTime*60f));
      footRotate.update();
      pose_3X -= 0.04f;
      pose_3Z -= 0.03f;
    }
    else if (neckAngle < 20) {
      neckRotate.setTransform(Mat4Transform.rotateAroundZ(-neckAngle));
      neckRotate.update();
    }
  }

  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

}