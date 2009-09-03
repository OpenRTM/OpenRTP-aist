package _SDOPackage;


/**
* _SDOPackage/ParameterHelper.java .
* IDL-to-Java コンパイラ (ポータブル), バージョン "3.1" で生成
* 生成元: svn/jp.go.aist.rtm.systemeditor/idl/SDOPackage11.idl
* 2008年12月4日 (木曜日) 12時46分40秒 JST
*/

abstract public class ParameterHelper
{
  private static String  _id = "IDL:org.omg/SDOPackage/Parameter:1.0";

  public static void insert (org.omg.CORBA.Any a, _SDOPackage.Parameter that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static _SDOPackage.Parameter extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "name",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_TypeCode);
          _members0[1] = new org.omg.CORBA.StructMember (
            "type",
            _tcOf_members0,
            null);
          _tcOf_members0 = _SDOPackage.AllowedValuesHelper.type ();
          _members0[2] = new org.omg.CORBA.StructMember (
            "allowed_values",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (_SDOPackage.ParameterHelper.id (), "Parameter", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static _SDOPackage.Parameter read (org.omg.CORBA.portable.InputStream istream)
  {
    _SDOPackage.Parameter value = new _SDOPackage.Parameter ();
    value.name = istream.read_string ();
    value.type = istream.read_TypeCode ();
    value.allowed_values = _SDOPackage.AllowedValuesHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, _SDOPackage.Parameter value)
  {
    ostream.write_string (value.name);
    ostream.write_TypeCode (value.type);
    _SDOPackage.AllowedValuesHelper.write (ostream, value.allowed_values);
  }

}
