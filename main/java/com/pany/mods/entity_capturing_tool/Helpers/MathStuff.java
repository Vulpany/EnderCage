package com.pany.mods.entity_capturing_tool.Helpers;

import net.minecraft.client.realms.Request;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MathStuff {

    public static <PosType> double[] PosToXYZ(PosType pos) {
        double[] Return = new double[3];
        if (pos instanceof BlockPos) {
            Return[0] = ((BlockPos) pos).getX();
            Return[1] = ((BlockPos) pos).getY();
            Return[2] = ((BlockPos) pos).getZ();
        } else if (pos instanceof Vec3d) {
            Return[0] = ((Vec3d) pos).getX();
            Return[1] = ((Vec3d) pos).getY();
            Return[2] = ((Vec3d) pos).getZ();
        } else if (pos instanceof Vec3i) {
            Return[0] = ((Vec3i) pos).getX();
            Return[1] = ((Vec3i) pos).getY();
            Return[2] = ((Vec3i) pos).getZ();
        } else {
            Return[0] = 0;
            Return[1] = 0;
            Return[2] = 0;
        }
        return Return;
    }

    public static double GetDistance(double Start,double End) {
        return Math.abs(End-Start);
    }
    public static double GetDistance(double StartX,double StartZ,double EndX,double EndZ) {
        return Math.pow(Math.pow(EndX-StartX,2) + Math.pow(StartZ-EndZ,2),0.5);
    }
    public static double GetDistance(double StartX,double StartY,double StartZ,double EndX,double EndY,double EndZ) {
        return Math.pow(Math.pow(EndX-StartX,2) + Math.pow(StartY-EndY,2) + Math.pow(StartZ-EndZ,2),0.5);
    }

    public static <PosType1,PosType2> double GetDistance(PosType1 Start,PosType2 End) {
        double[] XYZStart = PosToXYZ(Start);
        double[] XYZEnd = PosToXYZ(End);
        return GetDistance(XYZStart[0],XYZStart[1],XYZStart[2],XYZEnd[0],XYZEnd[1],XYZEnd[2]);
    }

    public static double Dot(double Number) {
        return Number > 0 ? 1 : -1;
    }

    public static double[] Dot(double X,double Z) {
        double Distance = GetDistance(0,0,X,Z);
        double[] Direction = {X/Distance,Z/Distance};
        return Direction;
    }

    public static double[] Dot(double X,double Y,double Z) {
        double Distance = GetDistance(0,0,0,X,Y,Z);
        double[] Direction = {X/Distance,Y/Distance,Z/Distance};
        return Direction;
    }

    public static BlockPos Dot(BlockPos Pos) {
        double Distance = GetDistance(new BlockPos(0,0,0),Pos);
        return new BlockPos((int)(Pos.getX()/Distance),(int)(Pos.getY()/Distance),(int)(Pos.getZ()/Distance) );
    }
    public static Vec3d Dot(Vec3d Pos) {
        double Distance = GetDistance(new Vec3d(0,0,0),Pos);
        return new Vec3d((int)(Pos.getX()/Distance),(int)(Pos.getY()/Distance),(int)(Pos.getZ()/Distance) );
    }

    public static double AngleToPI(double Angle) {
        return (Angle/360)*(Math.PI*2);
    }

    public static double PIToAngle(double PIVal) {
        return 360/((Math.PI*2)/PIVal);
    }

    public static double[] DrawCircle(double Angle) {
        double Val = AngleToPI(Angle);
        double X = Math.sin(Val);
        double Z = Math.cos(Val);
        double[] Pos = new double[2];
        Pos[0] = X;Pos[1] = Z;
        return Pos;
    }

    public static double[] DrawCircle(double Angle,double Radius) {
        double Val = AngleToPI(Angle);
        double X = Math.sin(Val)*Radius;
        double Z = Math.cos(Val)*Radius;
        double[] Pos = new double[2];
        Pos[0] = X;Pos[1] = Z;
        return Pos;
    }

    public static double[] DrawBasicCircle(double PIAngle) {
        double X = Math.sin(PIAngle);
        double Z = Math.cos(PIAngle);
        double[] Pos = new double[2];
        Pos[0] = X;Pos[1] = Z;
        return Pos;
    }

    public static double[] DrawBasicCircle(double PIAngle,double Radius) {
        double X = Math.sin(PIAngle)*Radius;
        double Z = Math.cos(PIAngle)*Radius;
        double[] Pos = new double[2];
        Pos[0] = X;Pos[1] = Z;
        return Pos;
    }

    public static double OffsetToAngle(double X,double Z) {
        double[] Pos = Dot(X,Z);
        return Math.atan2(Pos[0],Pos[1]);
    }

    public static double OffsetToPIAngle(double X,double Z) {
        double[] Pos = Dot(X,Z);
        return Math.atan2(Pos[0],Pos[1]);
    }

    public static double Lerp(double From,double To,double Percentage) {
        return From + (To-From)*Percentage;
    }

    public static double LerpAngle(double From,double To,double Percentage) {
        if (GetDistance(From,To+360) < GetDistance(From,To)) {
            To += 360;
        }
        return Lerp(From,To,Percentage)%360;
    }

    public static double LerpPIAngle(double From,double To,double Percentage) {
        if (GetDistance(From,To+(Math.PI*2)) < GetDistance(From,To)) {
            To += (Math.PI*2);
        }
        // System.out.println(From + "," + To + "," + Percentage);
        return Lerp(From,To,Percentage)%(Math.PI*2);
    }
    // Lazy
    public static double DiffMathRandom() {
        return Math.random() - 0.5;
    }

}
