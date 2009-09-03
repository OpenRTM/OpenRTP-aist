package RTC;


/**
* RTC/MultiModeObjectHelper.java .
* IDL-to-Java コンパイラ (ポータブル), バージョン "3.1" で生成
* 生成元: svn/jp.go.aist.rtm.systemeditor/idl/RTC10.idl
* 2008年12月4日 (木曜日) 14時12分45秒 JST
*/


/*!
   * @if jp
   * @brief 
   * @else
   * @brief 
   * @endif
   */
abstract public class MultiModeObjectHelper
{
  private static String  _id = "IDL:omg.org/RTC/MultiModeObject:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.MultiModeObject that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.MultiModeObject extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (RTC.MultiModeObjectHelper.id (), "MultiModeObject");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.MultiModeObject read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_MultiModeObjectStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.MultiModeObject value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static RTC.MultiModeObject narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.MultiModeObject)
      return (RTC.MultiModeObject)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._MultiModeObjectStub stub = new RTC._MultiModeObjectStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
