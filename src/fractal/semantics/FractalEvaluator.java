 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.semantics;
import cs34q.turtle.Turtle;
import fractal.syntax.ASTDefine;
import fractal.syntax.ASTExpAdd;
import fractal.syntax.ASTExpDiv;
import fractal.syntax.ASTExpLit;
import fractal.syntax.ASTExpMod;
import fractal.syntax.ASTExpMul;
import fractal.syntax.ASTExpSub;
import fractal.syntax.ASTExpVar;
import fractal.syntax.ASTFracCompose;
import fractal.syntax.ASTFracInvocation;
import fractal.syntax.ASTFracSequence;
import fractal.syntax.ASTFracVar;
import fractal.syntax.ASTFractal;
import fractal.syntax.ASTRender;
import fractal.syntax.ASTRepeat;
import fractal.syntax.ASTRestoreStmt;
import fractal.syntax.ASTSaveStmt;
import fractal.syntax.ASTSelf;
import fractal.syntax.ASTStmtSequence;
import fractal.syntax.ASTTCmdBack;
import fractal.syntax.ASTTCmdClear;
import fractal.syntax.ASTTCmdForward;
import fractal.syntax.ASTTCmdHome;
import fractal.syntax.ASTTCmdLeft;
import fractal.syntax.ASTTCmdPenDown;
import fractal.syntax.ASTTCmdPenUp;
import fractal.syntax.ASTTCmdRight;
import fractal.sys.FractalException;
import fractal.values.FractalValue;
import fractal.values.PrimitiveFractal;
import java.util.*;
import fractal.syntax.ASTStatement;
import fractal.sys.FractalRestoreException;
import fractal.values.Fractal;
import fractal.syntax.ASTFracExp;

/**
 *
 * @author newts
 */
public class FractalEvaluator extends AbstractFractalEvaluator {


    @Override
    public FractalValue visitASTStmtSequence(ASTStmtSequence seq, FractalState state) throws FractalException {
      ASTStatement s1;
      ArrayList<ASTStatement> s2 = seq.getSeq();
      Iterator iter = s2.iterator();
      FractalValue result = FractalValue.NO_VALUE;
      while(iter.hasNext()){
        s1 = (ASTStatement) iter.next();
        result = s1.visit(this, state);
      }
      return result;
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTSaveStmt(ASTSaveStmt form, FractalState state) throws FractalException {
        state.pushTurtle();
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTRestoreStmt(ASTRestoreStmt form, FractalState state) throws FractalException {
      try {
        state.popTurtle();
      }

      catch (EmptyStackException e) {
        throw new FractalRestoreException();
      }
      return FractalValue.NO_VALUE;
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTRender(ASTRender form, FractalState state) throws FractalException {
      ASTFracExp fractal = form.getFractal();
      fractal.visit(this, state);

      state.setDefaultLevel(form.getLevel().visit(this,state).intValue());
      state.setDefaultScale(form.getScale().visit(this,state).realValue());
      //invokeFractal(fractal.fractalValue(), state);
      state.updateDisplay();
      return FractalValue.NO_VALUE;
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTDefine(ASTDefine form, FractalState state) throws FractalException {
      Environment env = state.getEnvironment();
      env.put(form.getVar(), form.getValueExp().visit(this, state));
      return FractalValue.NO_VALUE;
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTRepeat(ASTRepeat form, FractalState state) throws FractalException {
        int start = form.getCountExp().visit(this, state).intValue();
        Environment env = state.getEnvironment();
        FractalValue result = FractalValue.NO_VALUE;
        env.put(form.getLoopVar(), form.getCountExp().visit(this,state));
        for(int i = 0; i< start;i++){
          result = form.getBody().visit(this,state);
        }
        return result;
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracInvocation(ASTFracInvocation form, FractalState state) throws FractalException {
        invokeFractal(form.getFractal(),state);
        return FractalValue.NO_VALUE;
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracSequence(ASTFracSequence form, FractalState state) throws FractalException {
	// Create and return an instance of SequencedFractal here
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracCompose(ASTFracCompose form, FractalState state) throws FractalException {
	// Create and return an instance of CompositeFractal here
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracVar(ASTFracVar form, FractalState state) throws FractalException {
        Environment current = state.getEnvironment();
        return current.get(form.getVar());

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFractal(ASTFractal form, FractalState state) throws FractalException {
	// Create and return an instance of a PrimitiveFractal here
        PrimitiveFractal fractal = new PrimitiveFractal(state.getCurrentScale(),form.getBody(),state);
        return fractal;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTSelf(ASTSelf form, FractalState state) throws FractalException {
        Fractal fractal = state.getCurrentFractal();
        int level =  state.getCurrentLevel();
        double scale = state.getCurrentScale();
        InvocationContext context = new InvocationContext(fractal,level,scale);
        state.extendContext(context);
        invokeFractal(fractal,state);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdLeft(ASTTCmdLeft form, FractalState state) throws FractalException {
        FractalValue angleVal = form.getAngle().visit(this, state);
        double angle = angleVal.realValue();
        state.getTurtleState().turn(angle);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdRight(ASTTCmdRight form, FractalState state) throws FractalException {
        FractalValue angleVal = form.getAngle().visit(this, state);
        double angle = angleVal.realValue();
        state.getTurtleState().turn(-angle);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdForward(ASTTCmdForward form, FractalState state) throws FractalException {
        FractalValue distVal = form.getLength().visit(this, state);
        Double dist = distVal.realValue();
        displaceTurtle(state, dist);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdBack(ASTTCmdBack form, FractalState state) throws FractalException {
        FractalValue distVal = form.getLength().visit(this, state);
        Double dist = distVal.realValue();
        displaceTurtle(state, -dist);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdPenDown(ASTTCmdPenDown form, FractalState state) throws FractalException {
      Turtle turtle = state.getTurtleState();
      turtle = turtle.derivePenDown(true);
      return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdPenUp(ASTTCmdPenUp form, FractalState state) throws FractalException {
        Turtle turtle = state.getTurtleState();

        turtle = turtle.derivePenDown(false);
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdClear(ASTTCmdClear form, FractalState state) throws FractalException {
        state.getDisplay().clear();
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdHome(ASTTCmdHome form, FractalState state) throws FractalException {
        Turtle turtle = state.getTurtleState();
        turtle.home();
        return FractalValue.NO_VALUE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpAdd(ASTExpAdd form, FractalState state) throws FractalException {
      FractalValue first = form.getFirst().visit(this, state);
      FractalValue second = form.getSecond().visit(this, state);
      return first.add(second);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpSub(ASTExpSub form, FractalState state) throws FractalException {
      FractalValue first = form.getFirst().visit(this, state);
      FractalValue second = form.getSecond().visit(this, state);
      return first.sub(second);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpMul(ASTExpMul form, FractalState state) throws FractalException {
      FractalValue first = form.getFirst().visit(this, state);
      FractalValue second = form.getSecond().visit(this, state);
      return first.mul(second);
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpDiv(ASTExpDiv form, FractalState state) throws FractalException {
      FractalValue first = form.getFirst().visit(this, state);
      FractalValue second = form.getSecond().visit(this, state);
      return first.div(second);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpMod(ASTExpMod form, FractalState state) throws FractalException {
      FractalValue first = form.getFirst().visit(this, state);
      FractalValue second = form.getSecond().visit(this, state);
      return first.mod(second);
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpLit(ASTExpLit form, FractalState state) throws FractalException {
        return form.getValue();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpVar(ASTExpVar form, FractalState state) throws FractalException {
        Environment env =  state.getEnvironment();
        FractalValue val = env.get(form.getVar());
        return val;
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
