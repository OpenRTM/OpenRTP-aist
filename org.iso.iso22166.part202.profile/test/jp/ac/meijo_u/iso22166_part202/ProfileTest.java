package jp.ac.meijo_u.iso22166_part202;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.iso.iso22166.part202.profile.ArgSpec;
import org.iso.iso22166.part202.profile.Communication;
import org.iso.iso22166.part202.profile.CompilerType;
import org.iso.iso22166.part202.profile.CyberSecurity;
import org.iso.iso22166.part202.profile.DataBus;
import org.iso.iso22166.part202.profile.ExeForm;
import org.iso.iso22166.part202.profile.ExeStatus;
import org.iso.iso22166.part202.profile.ExecutableForm;
import org.iso.iso22166.part202.profile.ExecutionType;
import org.iso.iso22166.part202.profile.IDnType;
import org.iso.iso22166.part202.profile.IOVariables;
import org.iso.iso22166.part202.profile.InOutType;
import org.iso.iso22166.part202.profile.InfraType;
import org.iso.iso22166.part202.profile.Infrastructure;
import org.iso.iso22166.part202.profile.InstanceType;
import org.iso.iso22166.part202.profile.Libraries;
import org.iso.iso22166.part202.profile.Library;
import org.iso.iso22166.part202.profile.MOType;
import org.iso.iso22166.part202.profile.ModelCase;
import org.iso.iso22166.part202.profile.Modelling;
import org.iso.iso22166.part202.profile.ModuleID;
import org.iso.iso22166.part202.profile.NVList;
import org.iso.iso22166.part202.profile.NameValue;
import org.iso.iso22166.part202.profile.NoBit;
import org.iso.iso22166.part202.profile.OStype;
import org.iso.iso22166.part202.profile.ObjectFactory;
import org.iso.iso22166.part202.profile.OpTypes;
import org.iso.iso22166.part202.profile.PLSILType;
import org.iso.iso22166.part202.profile.PhysicalVirtual_0020;
import org.iso.iso22166.part202.profile.Properties;
import org.iso.iso22166.part202.profile.Property;
import org.iso.iso22166.part202.profile.RangeString;
import org.iso.iso22166.part202.profile.ReqProvType;
import org.iso.iso22166.part202.profile.SIM;
import org.iso.iso22166.part202.profile.SafeSecure;
import org.iso.iso22166.part202.profile.SafetyFunction;
import org.iso.iso22166.part202.profile.SafetyType;
import org.iso.iso22166.part202.profile.SafeytLevelPL;
import org.iso.iso22166.part202.profile.SecurityType;
import org.iso.iso22166.part202.profile.ServiceMethod;
import org.iso.iso22166.part202.profile.ServiceProfile;
import org.iso.iso22166.part202.profile.Services;
import org.iso.iso22166.part202.profile.Status;
import org.iso.iso22166.part202.profile.Variable;
import org.openrtp.namespaces.ros.version01.RosProfile;
import org.openrtp.namespaces.rtc.version03.RtcProfile;

import jp.ac.meijo_u.iso22166_part202.util.ISO2ROSProfileHandler;
import jp.ac.meijo_u.iso22166_part202.util.ISO2RTCProfileHandler;
import jp.ac.meijo_u.iso22166_part202.util.ROS2ISOProfileHandler;
import jp.ac.meijo_u.iso22166_part202.util.RTC2ISOProfileHandler;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandlerROS;	

public class ProfileTest {
	private String rootPath;
	
	public ProfileTest() {
		File fileCurrent = new File(".");
		rootPath = fileCurrent.getAbsolutePath();
		rootPath = rootPath.substring(0,rootPath.length()-1);
	}

	public void makeIsoProfile() {
		ObjectFactory factory = new ObjectFactory();
		
		SIM sim = factory.createSIM();
		sim.setModuleName("mod01");
		sim.setDescription("mod01Desc");
		sim.setManufacturer("Vecdor01");
		sim.setExamples("Mod01Example");
		
		{
			IDnType iDnType = factory.createIDnType();
			sim.setIdnType(iDnType);
			
			ModuleID moduleId = factory.createModuleID();
			moduleId.setIID(hexStringToBytes("1"));
			moduleId.getMID().add(hexStringToBytes("2345678901"));
			iDnType.setModuleID(moduleId);
			iDnType.setInformationModelVersion("1.3.5");
			
			ModuleID sw01 = factory.createModuleID();
			sw01.setIID(new byte[] {9});
			sw01.getMID().add(new byte[] {8, 7, 6, 5, 4, 3, 2, 1, 0});
			iDnType.getSwAspects().add(sw01);

			ModuleID sw02 = factory.createModuleID();
			sw02.setIID(new byte[] {8});
			sw02.getMID().add(new byte[] {1, 3, 5, 7, 9, 2, 4, 6, 8});
			iDnType.getSwAspects().add(sw02);

			ModuleID sw03 = factory.createModuleID();
			sw03.setIID(new byte[] {5});
			sw03.getMID().add(new byte[] {0, 5, 1, 0, 1, 5, 2, 0, 2, 5});
			iDnType.getSwAspects().add(sw03);
		}
		
		{
			Properties properties = factory.createProperties();
			sim.setProperties(properties);
			
			OStype oStype = factory.createOStype();
			oStype.setType("Windows");
			oStype.setVersion("11");
			oStype.setBit(NoBit.BIT_64);
			properties.setOsType(oStype);
			
			ExecutionType et01 = factory.createExecutionType();
			et01.setOpType(OpTypes.PERIODIC);
			et01.setHardRT(false);
			et01.setTimeConstraint(0.5);
			et01.setPriority(new byte[] { 1 });
			et01.setInstanceType(InstanceType.SINGLETON);
			properties.getExeType().add(et01);

			ExecutionType et02 = factory.createExecutionType();
			et02.setOpType(OpTypes.EVENTDRIVEN);
			et02.setHardRT(true);
			et02.setTimeConstraint(0.1);
			et02.setPriority(new byte[] { 2 });
			et02.setInstanceType(InstanceType.MULTITON_STATIC);
			properties.getExeType().add(et02);
			
			ExecutionType et03 = factory.createExecutionType();
			et03.setOpType(OpTypes.NONRT);
			et03.setHardRT(false);
			et03.setTimeConstraint(1.0);
			et03.setPriority(new byte[] { 10 });
			et03.setInstanceType(InstanceType.MULTITON_COMM);
			properties.getExeType().add(et03);
			
			CompilerType compiler = factory.createCompilerType();
			compiler.setOsName("Windows");
			RangeString range = factory.createRangeString();
			range.setMax("10");
			range.setMax("11");
			compiler.setVerRangeCompiler(range);
			compiler.setCompilerName("VisualStudio");
			RangeString cRange = factory.createRangeString();
			cRange.setMin("3.4.5");
			cRange.setMax("5.6.7");
			compiler.setVerRangeCompiler(cRange);
			compiler.setBitnCPUarch("64Bit");
			properties.setCompiler(compiler);
			
			Libraries libs = factory.createLibraries();
			properties.setLibs(libs);
			
			Library lib01 = factory.createLibrary();
			lib01.setName("xxx");
			lib01.setVersion("1.2.3");
			libs.getLibraries().add(lib01);
			
			Library lib02 = factory.createLibrary();
			lib02.setName("yyy");
			lib02.setVersion("4.5.6");
			libs.getLibraries().add(lib02);
			
			Library lib03 = factory.createLibrary();
			lib03.setName("zzz");
			lib03.setVersion("7.8.9");
			libs.getLibraries().add(lib03);
			
			Property prop01 = factory.createProperty();
			prop01.setName("Prop01");
			prop01.setDescription("prop01Desc");
			prop01.setType("String");
			prop01.setUnit("prop01Unit");
			prop01.setValue("Val01");
			prop01.setImmutable(false);
			properties.getProperty().add(prop01);
			
			Property prop02 = factory.createProperty();
			prop02.setName("Prop02");
			prop02.setDescription("prop02Desc");
			prop02.setType("int");
			prop02.setUnit("prop02Unit");
			prop02.setValue("Val02");
			prop02.setImmutable(true);
			properties.getProperty().add(prop02);
			
			Property prop03 = factory.createProperty();
			prop03.setName("Prop03");
			prop03.setDescription("prop03Desc");
			prop03.setType("float");
			prop03.setUnit("prop03Unit");
			prop03.setValue("Val03");
			prop03.setImmutable(true);
			properties.getProperty().add(prop03);
		}
		
		{
			IOVariables ioVariables = factory.createIOVariables();
			sim.setIoVariables(ioVariables);
			
			Variable var01 = factory.createVariable();
			var01.setName("Var01");
			var01.setDescription("var01Desc");
			var01.setType("String");
			var01.setUnit("var01Unit");
			var01.setIoType(InOutType.IN);
			var01.setValue("val01");
			ioVariables.getVariable().add(var01);
			
			NVList nv = factory.createNVList();
			var01.setAdditionalInfo(nv);
			NameValue nv01 = factory.createNameValue();
			nv01.setName("nv01");
			nv01.setValue("val01");
			nv.getNv().add(nv01);

			NameValue nv02 = factory.createNameValue();
			nv02.setName("nv02");
			nv02.setValue("val02");
			nv.getNv().add(nv02);
			
			NameValue nv03 = factory.createNameValue();
			nv03.setName("nv03");
			nv03.setValue("val03");
			nv.getNv().add(nv03);
			
			Variable var02 = factory.createVariable();
			var02.setName("Var02");
			var02.setDescription("var02Desc");
			var02.setType("float");
			var02.setUnit("var02Unit");
			var02.setIoType(InOutType.OUT);
			var02.setValue("val02");
			ioVariables.getVariable().add(var02);
			
			Variable var03 = factory.createVariable();
			var03.setName("Var03");
			var03.setDescription("var03Desc");
			var03.setType("int");
			var03.setUnit("var03Unit");
			var03.setIoType(InOutType.OUT);
			var03.setValue("val03");
			ioVariables.getVariable().add(var03);
		}
		
		{
			Status status = factory.createStatus();
			sim.setStatus(status);
			
			status.setExecutionStatus(ExeStatus.CREATE);
			status.setErrorType(new BigInteger("5"));
		}
		
		{
			Services services = factory.createServices();
			sim.setServices(services);
			
			services.setNoOfBasicService(new BigInteger("2"));
			services.setNoOfOptionalService(new BigInteger("1"));
			
			ServiceProfile sv01 = factory.createServiceProfile();
			sv01.setId("sv01");
			sv01.setIfURL("xxx.idl");
			sv01.setPvType(PhysicalVirtual_0020.PHYSICAL);
			sv01.setMoType(MOType.MANDATORY);
			services.getServiceProfile().add(sv01);
			
			NVList nv = factory.createNVList();
			sv01.setAdditionalInfo(nv);
			NameValue nv01 = factory.createNameValue();
			nv01.setName("svxxx");
			nv01.setValue("svVl01");
			nv.getNv().add(nv01);
			
			NameValue nv02 = factory.createNameValue();
			nv02.setName("svyyy");
			nv02.setValue("svVl02");
			nv.getNv().add(nv02);
			
			NameValue nv03 = factory.createNameValue();
			nv03.setName("svzzz");
			nv03.setValue("svVl03");
			nv.getNv().add(nv03);
			
			ServiceMethod sm0101 = factory.createServiceMethod();
			sm0101.setMethodName("method0101");
			sm0101.setRetType("void");
			sm0101.setReqProvType(ReqProvType.PROVIDED);
			sm0101.setMoType(MOType.MANDATORY);
			sv01.getMethodList().add(sm0101);
			
			NVList nvm = factory.createNVList();
			sm0101.setAdditionalInfo(nvm);
			NameValue nv01m = factory.createNameValue();
			nv01m.setName("mexxx");
			nv01m.setValue("mel01");
			nvm.getNv().add(nv01m);
			
			NameValue nv02m = factory.createNameValue();
			nv02m.setName("meyyy");
			nv02m.setValue("mel02");
			nvm.getNv().add(nv02m);
			
			NameValue nv03m = factory.createNameValue();
			nv03m.setName("mezzz");
			nv03m.setValue("mel03");
			nvm.getNv().add(nv03m);
			
			ArgSpec arg01 = factory.createArgSpec();
			arg01.setValueName("Arg01");
			arg01.setType("type01");
			arg01.setInout(InOutType.IN);
			sm0101.getArgType().add(arg01);
			
			NVList nva = factory.createNVList();
			arg01.setAdditionalInfo(nva);
			NameValue nv01a = factory.createNameValue();
			nv01a.setName("argxxx");
			nv01a.setValue("argl01");
			nva.getNv().add(nv01a);
			
			NameValue nv02a = factory.createNameValue();
			nv02a.setName("argyyy");
			nv02a.setValue("arg02");
			nva.getNv().add(nv02a);
			
			NameValue nv03a = factory.createNameValue();
			nv03a.setName("argzzz");
			nv03a.setValue("arg03");
			nva.getNv().add(nv03a);
			
			ArgSpec arg02 = factory.createArgSpec();
			arg02.setValueName("Arg02");
			arg02.setType("type02");
			arg02.setInout(InOutType.IN);
			sm0101.getArgType().add(arg02);
			
			ArgSpec arg03 = factory.createArgSpec();
			arg03.setValueName("Arg03");
			arg03.setType("type03");
			arg03.setInout(InOutType.IN);
			sm0101.getArgType().add(arg03);
			/////
			ServiceMethod sm0102 = factory.createServiceMethod();
			sm0102.setMethodName("method0102");
			sm0102.setRetType("int");
			sm0102.setReqProvType(ReqProvType.PROVIDED);
			sm0102.setMoType(MOType.MANDATORY);
			sv01.getMethodList().add(sm0102);
			
			ArgSpec arg11 = factory.createArgSpec();
			arg11.setValueName("Arg11");
			arg11.setType("type11");
			arg11.setInout(InOutType.IN);
			sm0102.getArgType().add(arg11);
			
			ArgSpec arg12 = factory.createArgSpec();
			arg12.setValueName("Arg12");
			arg12.setType("type12");
			arg12.setInout(InOutType.IN);
			sm0102.getArgType().add(arg12);
			
			ServiceMethod sm0103 = factory.createServiceMethod();
			sm0103.setMethodName("method0103");
			sm0103.setRetType("float");
			sm0103.setReqProvType(ReqProvType.REQUIRED);
			sm0103.setMoType(MOType.OPTIONAL);
			sv01.getMethodList().add(sm0103);
			//////////
			ServiceProfile sv02 = factory.createServiceProfile();
			sv02.setId("sv02");
			sv02.setIfURL("yyy.idl");
			sv02.setPvType(PhysicalVirtual_0020.PHYSICAL);
			sv02.setMoType(MOType.MANDATORY);
			services.getServiceProfile().add(sv02);
			
			ServiceMethod sm02 = factory.createServiceMethod();
			sm02.setMethodName("method02");
			sm02.setRetType("float");
			sm02.setReqProvType(ReqProvType.PROVIDED);
			sm02.setMoType(MOType.MANDATORY);
			sv02.getMethodList().add(sm02);
			//////////
			ServiceProfile sv03 = factory.createServiceProfile();
			sv03.setId("sv03");
			sv03.setIfURL("zzz.idl");
			sv03.setPvType(PhysicalVirtual_0020.VIRTUAL);
			sv03.setMoType(MOType.OPTIONAL);
			services.getServiceProfile().add(sv03);
			
			ServiceMethod sm03 = factory.createServiceMethod();
			sm03.setMethodName("method03");
			sm03.setRetType("int");
			sm03.setReqProvType(ReqProvType.PROVIDED);
			sm03.setMoType(MOType.MANDATORY);
			sv03.getMethodList().add(sm03);
		}
		
		{
			Infrastructure infra = factory.createInfrastructure();
			sim.setInfra(infra);
			
			InfraType db01 = factory.createInfraType();
			db01.setName("db01");
			RangeString range01 = factory.createRangeString();
			range01.setMin("1.2");
			range01.setMax("3.4");
			db01.setVersion(range01);
			infra.getDatabase().add(db01);
			
			InfraType db02 = factory.createInfraType();
			db02.setName("db02");
			RangeString range02 = factory.createRangeString();
			range02.setMin("5.6");
			range02.setMax("7.8");
			db02.setVersion(range02);
			infra.getDatabase().add(db02);
			
			InfraType db03 = factory.createInfraType();
			db03.setName("db03");
			RangeString range03 = factory.createRangeString();
			range03.setMin("9.8");
			range03.setMax("7.6");
			db03.setVersion(range03);
			infra.getDatabase().add(db03);
			
			InfraType mid01 = factory.createInfraType();
			mid01.setName("mid01");
			RangeString range01m = factory.createRangeString();
			range01m.setMin("1.2");
			range01m.setMax("3.4");
			mid01.setVersion(range01m);
			infra.getMiddleware().add(mid01);
			
			InfraType mid02 = factory.createInfraType();
			mid02.setName("mid02");
			RangeString range02m = factory.createRangeString();
			range02m.setMin("5.6");
			range02m.setMax("7.8");
			mid02.setVersion(range02m);
			infra.getMiddleware().add(mid02);
			
			InfraType mid03 = factory.createInfraType();
			mid03.setName("mid03");
			RangeString range03m = factory.createRangeString();
			range03m.setMin("9.8");
			range03m.setMax("7.6");
			mid03.setVersion(range03m);
			infra.getMiddleware().add(mid03);
			//////////
			Communication com01 = factory.createCommunication();
			infra.getComms().add(com01);

			InfraType mt01 = factory.createInfraType();
			mt01.setName("mid01");
			RangeString range01mt = factory.createRangeString();
			range01mt.setMin("1.2");
			range01mt.setMax("3.4");
			mt01.setVersion(range01mt);
			com01.getMostTopProtocol().add(mt01);
			
			InfraType mt02 = factory.createInfraType();
			mt02.setName("mid02");
			RangeString range02mt = factory.createRangeString();
			range02mt.setMin("5.7");
			range02mt.setMax("6.8");
			mt02.setVersion(range02mt);
			com01.getMostTopProtocol().add(mt02);
			
			InfraType mt03 = factory.createInfraType();
			mt03.setName("mid03");
			RangeString range03mt = factory.createRangeString();
			range03mt.setMin("9.1");
			range03mt.setMax("2.7");
			mt03.setVersion(range03mt);
			com01.getMostTopProtocol().add(mt03);
			
			DataBus up = factory.createDataBus();
			com01.setUnderlyingProtocol(up);
			up.setConnectionType("USB-A");
			up.setTypePhyMac("USB");
			up.getTypeNetTrans().add("TCP/IP");
			up.getTypeNetTrans().add("TCP");
			up.getTypeNetTrans().add("IP");
			up.getTypeApp().add("FTP");
			up.getTypeApp().add("HTTP");
			up.getTypeApp().add("DNS");
			up.setSpeed(3.7);
			
			NVList nvd = factory.createNVList();
			up.setAdditionalInfo(nvd);
			NameValue nv01 = factory.createNameValue();
			nv01.setName("nv01");
			nv01.setValue("val01");
			nvd.getNv().add(nv01);
			
			NameValue nv02 = factory.createNameValue();
			nv02.setName("nv02");
			nv02.setValue("val02");
			nvd.getNv().add(nv02);
			
			NameValue nv03 = factory.createNameValue();
			nv03.setName("nv03");
			nv03.setValue("val03");
			nvd.getNv().add(nv03);
			
			Communication com02 = factory.createCommunication();
			infra.getComms().add(com02);

			InfraType mt201 = factory.createInfraType();
			mt201.setName("mid201");
			RangeString range01mt2 = factory.createRangeString();
			range01mt2.setMin("8.3");
			range01mt2.setMax("6.4");
			mt201.setVersion(range01mt2);
			com02.getMostTopProtocol().add(mt201);
			
			DataBus up2 = factory.createDataBus();
			com02.setUnderlyingProtocol(up2);
			up2.setConnectionType("USB-C");
			up2.setTypePhyMac("USB2");
			up2.getTypeNetTrans().add("IP");
			up2.getTypeApp().add("BOOTP");
			up2.setSpeed(4.1);
			
			Communication com03 = factory.createCommunication();
			infra.getComms().add(com03);

			InfraType mt301 = factory.createInfraType();
			mt301.setName("mid301");
			RangeString range01mt3 = factory.createRangeString();
			range01mt3.setMin("6.3");
			range01mt3.setMax("8.7");
			mt301.setVersion(range01mt3);
			com03.getMostTopProtocol().add(mt301);
			
			DataBus up3 = factory.createDataBus();
			com03.setUnderlyingProtocol(up3);
			up3.setConnectionType("RJ45");
			up3.setTypePhyMac("EtherCAT");
			up3.getTypeNetTrans().add("TCP");
			up3.getTypeApp().add("XXX");
			up3.setSpeed(4.1);
		}
		
		{
			SafeSecure safeSecure = factory.createSafeSecure();
			sim.setSafeSecure(safeSecure);
			
			safeSecure.setOverallValidSafetyLevelType(PLSILType.PL);
			safeSecure.setOverallSafetyLevelPL(SafeytLevelPL.C);
			safeSecure.setOverallSafetyLevelSIL("3");
			safeSecure.setOverallPhySecurityLevel("4");
			safeSecure.setOverallCybSecurityLevel("5");
			
			SafetyFunction sf01 = factory.createSafetyFunction();
			sf01.setSafetyFunctionType(SafetyType.ESTOP);
			sf01.setValidSafetyLevelType(PLSILType.NONE);
			sf01.setEachSafetyLevelPL(SafeytLevelPL.B);
			sf01.setEachSafetyLevelSIL("4");
			safeSecure.getInSafetyLevel().add(sf01);
			
			SafetyFunction sf02 = factory.createSafetyFunction();
			sf02.setSafetyFunctionType(SafetyType.HCOLA);
			sf02.setValidSafetyLevelType(PLSILType.BOTH);
			sf02.setEachSafetyLevelPL(SafeytLevelPL.E);
			sf02.setEachSafetyLevelSIL("1");
			safeSecure.getInSafetyLevel().add(sf02);

			SafetyFunction sf03 = factory.createSafetyFunction();
			sf03.setSafetyFunctionType(SafetyType.LIMWS);
			sf03.setValidSafetyLevelType(PLSILType.PL);
			sf03.setEachSafetyLevelPL(SafeytLevelPL.D);
			sf03.setEachSafetyLevelSIL("3");
			safeSecure.getInSafetyLevel().add(sf03);
			
			CyberSecurity cs01 = factory.createCyberSecurity();
			cs01.setSecurityType(SecurityType.CRYTO);
			cs01.setEachSecurityLevel("2");
			safeSecure.getInCybSecurityLevel().add(cs01);
			
			CyberSecurity cs02 = factory.createCyberSecurity();
			cs02.setSecurityType(SecurityType.ACC_UNTRUST_NET);
			cs02.setEachSecurityLevel("2");
			safeSecure.getInCybSecurityLevel().add(cs02);
			
			CyberSecurity cs03 = factory.createCyberSecurity();
			cs03.setSecurityType(SecurityType.DO_S);
			cs03.setEachSecurityLevel("5");
			safeSecure.getInCybSecurityLevel().add(cs03);
			
			NVList nvd = factory.createNVList();
			safeSecure.setAdditionalInfo(nvd);
			
			NameValue nv01 = factory.createNameValue();
			nv01.setName("sf01");
			nv01.setValue("valsf01");
			nvd.getNv().add(nv01);
			
			NameValue nv02 = factory.createNameValue();
			nv02.setName("sf02");
			nv02.setValue("valsf02");
			nvd.getNv().add(nv02);
			
			NameValue nv03 = factory.createNameValue();
			nv03.setName("sf03");
			nv03.setValue("valsf03");
			nvd.getNv().add(nv03);
		}
		
		{
			Modelling modelling = factory.createModelling();
			sim.setModelling(modelling);
			
			ModelCase mc01 = factory.createModelCase();
			modelling.getSimulationModel().add(mc01);
			mc01.setSimulator("Sim01");
			mc01.getMdf().add("xxx");
			mc01.getMdf().add("yyy");
			mc01.getMdf().add("zzz");
			mc01.getLibraries().add("lib01");
			mc01.getLibraries().add("lib02");
			mc01.getLibraries().add("lib03");
			
			ExeForm ex01 = factory.createExeForm();
			mc01.getDynamicSW().add(ex01);
			ex01.setShellCmd("cmdxxx");
			ex01.setExeFileURL("url01");
			
			ExeForm ex02 = factory.createExeForm();
			mc01.getDynamicSW().add(ex02);
			ex02.setShellCmd("cmdyyy");
			ex02.setExeFileURL("url02");
			
			ExeForm ex03 = factory.createExeForm();
			mc01.getDynamicSW().add(ex03);
			ex03.setShellCmd("cmdzzz");
			ex03.setExeFileURL("url03");
						
			ModelCase mc02 = factory.createModelCase();
			modelling.getSimulationModel().add(mc02);
			mc02.setSimulator("Sim02");
			mc02.getMdf().add("xxx2");
			mc02.getLibraries().add("lib021");
			
			ExeForm ex201 = factory.createExeForm();
			mc02.getDynamicSW().add(ex201);
			ex201.setShellCmd("cmdxxx2");
			ex201.setExeFileURL("url012");
			
			ModelCase mc03 = factory.createModelCase();
			modelling.getSimulationModel().add(mc03);
			mc03.setSimulator("Sim03");
			mc03.getMdf().add("xxx3");
			mc03.getLibraries().add("lib031");
			
			ExeForm ex301 = factory.createExeForm();
			mc03.getDynamicSW().add(ex301);
			ex301.setShellCmd("cmdxxx3");
			ex301.setExeFileURL("url013");
		}
		
		{
			ExecutableForm exeForm = factory.createExecutableForm();
			sim.setExeForm(exeForm);
			exeForm.getLibraryURL().add("aaa");
			exeForm.getLibraryURL().add("bbb");
			exeForm.getLibraryURL().add("ccc");
			
			ExeForm ex01 = factory.createExeForm();
			ex01.setExeFileURL("ef01");
			ex01.setShellCmd("scm01");
			exeForm.getExeForm().add(ex01);
			
			ExeForm ex02 = factory.createExeForm();
			ex02.setExeFileURL("ef02");
			ex02.setShellCmd("scm02");
			exeForm.getExeForm().add(ex02);
			
			ExeForm ex03 = factory.createExeForm();
			ex03.setExeFileURL("ef03");
			ex03.setShellCmd("scm03");
			exeForm.getExeForm().add(ex03);
		}

		try {
			RTC2ISOProfileHandler isoHandler = new RTC2ISOProfileHandler();
			isoHandler.saveXmlIso(sim, "resource\\ISO_Base.xml");
		} catch(Exception ex) {
			int a = 0;
		}
	}

	private byte[] hexStringToBytes(String hex) {
	    int len = hex.length();
	    if (len % 2 != 0) return null;
	    
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                            + Character.digit(hex.charAt(i + 1), 16));
	    }
	    return data;
	}
	
	private String readFile(String fileName, String splitter) {
	    StringBuilder stbRet = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

	        String str;
	        while ((str = br.readLine()) != null) {
	            stbRet.append(str).append(splitter);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return stbRet.toString();
	}
	public void convertRtc2IsoProfile(String rtcPath, String isoPath) {
		String resourceDir = rootPath + rtcPath;
		String profileStr = readFile(resourceDir,"\n");
		String targetFile = rootPath + isoPath;
		
		XmlHandler handler = new XmlHandler();
		RtcProfile profile = null;
		try {
			profile = handler.restoreFromXmlRtc(profileStr);

			RTC2ISOProfileHandler isoHandler = new RTC2ISOProfileHandler();
			SIM isoProfile = isoHandler.convertRtc2Iso(profile);
			isoHandler.saveXmlIso(isoProfile, targetFile);
		} catch(Exception ex) {
			int a = 0;
		}
	}
	
	public void convertIso2RtcProfile(String isoPath, String rtcPath) {
		String resourceDir = rootPath + isoPath;
		String profileStr = readFile(resourceDir,"\n");
		String targetFile = rootPath + rtcPath;
		
		ISO2RTCProfileHandler handler = new ISO2RTCProfileHandler();
		SIM profile = null;
		try {
			profile = handler.restoreFromXmlIso(profileStr);
			RtcProfile rtcProfile = handler.convertIso2Rtc(profile);
			
			handler.saveXmlRtc(rtcProfile, targetFile);
		} catch(Exception ex) {
			int a = 0;
		}
	}
	
	public void convertROS2IsoProfile(String rtcPath, String isoPath) {
		String resourceDir = rootPath + rtcPath;
		String profileStr = readFile(resourceDir,"\n");
		String targetFile = rootPath + isoPath;
		
		XmlHandlerROS handler = new XmlHandlerROS();
		RosProfile profile = null;
		try {
			profile = handler.restoreFromXmlROS(profileStr);

			ROS2ISOProfileHandler isoHandler = new ROS2ISOProfileHandler();
			SIM isoProfile = isoHandler.convertROS2Iso(profile);
			isoHandler.saveXmlIso(isoProfile, targetFile);
		} catch(Exception ex) {
			int a = 0;
		}
	}

	public void convertIso2RosProfile(String isoPath, String rtcPath) {
		String resourceDir = rootPath + isoPath;
		String profileStr = readFile(resourceDir,"\n");
		String targetFile = rootPath + rtcPath;
		
		ISO2ROSProfileHandler handler = new ISO2ROSProfileHandler();
		SIM profile = null;
		try {
			profile = handler.restoreFromXmlIso(profileStr);
			RosProfile rosProfile = handler.convertIso2Ros(profile);
			
			handler.saveXmlRos(rosProfile, targetFile);
		} catch(Exception ex) {
			int a = 0;
		}
	}
	
	public static void main(String[] args) {
		ProfileTest test = new ProfileTest();
//		test.makeIsoProfile();
		
		//RTC - ISO - RTC 
		test.convertRtc2IsoProfile("\\resource\\RTC_Base.xml", "\\resource\\ISO.xml");
		test.convertIso2RtcProfile("\\resource\\ISO.xml", "\\resource\\RTC.xml");

		//ISO - RTC - ISO 
		test.convertIso2RtcProfile("\\resource\\ISO_Base.xml", "\\resource\\RTC_Conv.xml");
		test.convertRtc2IsoProfile("\\resource\\RTC_Conv.xml", "\\resource\\ISO_Conv.xml");
		test.convertIso2RtcProfile("\\resource\\ISO_Base_InOut.xml", "\\resource\\RTC_Conv_InOut.xml");
		test.convertRtc2IsoProfile("\\resource\\RTC_Conv_InOut.xml", "\\resource\\ISO_Conv_InOut.xml");

		//RTC - ISO - RTC 
		test.convertRtc2IsoProfile("\\resource\\RTC_Docker_Base.xml", "\\resource\\ISO_Docker.xml");
		test.convertIso2RtcProfile("\\resource\\ISO_Docker.xml", "\\resource\\RTC_Docker_Conv.xml");

		//ROS - ISO - ROS 
		test.convertROS2IsoProfile("\\resource\\ROS_Base.xml", "\\resource\\ISO_ROS.xml");
		test.convertIso2RosProfile("\\resource\\ISO_ROS.xml", "\\resource\\ROS.xml");

		//ISO - ROS - ISO 
		test.convertIso2RosProfile("\\resource\\ISO_Base.xml", "\\resource\\ROS_Conv.xml");
		test.convertROS2IsoProfile("\\resource\\ROS_Conv.xml", "\\resource\\ISO_Conv.xml");
	}

}
