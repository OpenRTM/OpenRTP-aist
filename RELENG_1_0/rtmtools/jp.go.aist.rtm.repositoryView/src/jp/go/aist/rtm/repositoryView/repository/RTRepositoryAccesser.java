package jp.go.aist.rtm.repositoryView.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.go.aist.rtm.repositoryView.model.RepositoryViewFactory;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewItem;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewLeafItem;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewRootItem;
import jp.go.aist.rtm.repositoryView.model.ServerRVRootItem;
import jp.go.aist.rtm.repositoryView.nl.Messages;
import jp.go.aist.rtm.repositoryView.ui.preference.RepositoryViewPreferenceManager;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ComponentSpecification;
import jp.go.aist.rtm.toolscommon.util.ProfileHandler;

import org.apache.commons.logging.Log;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.wizards.datatransfer.TarEntry;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.TarInputStream;
import org.openrtp.repository.ItemCategory;
import org.openrtp.repository.RTRepositoryAccessException;
import org.openrtp.repository.RTRepositoryClient;
import org.openrtp.repository.RTRepositoryClientFactory;


/**
 * RT���|�W�g���ɃA�N�Z�X���郆�[�e�B���e�B
 */
public class RTRepositoryAccesser {

	private static final String ZIP_EXT = "ZIP"; //$NON-NLS-1$
	private static final String TAR_EXT = "TAR"; //$NON-NLS-1$
	private static final String GZ_EXT = "GZ"; //$NON-NLS-1$
	//
	private static final String RTC_XML = "RTC.XML"; //$NON-NLS-1$
	//
	private static Log Logger;

	/**
	 * �V���O���g���C���X�^���X
	 */
	private static RTRepositoryAccesser __instance = new RTRepositoryAccesser();

	/**
	 * �V���O���g���ւ̃A�N�Z�T
	 * 
	 * @return �V���O���g��
	 */
	public static RTRepositoryAccesser getInstance() {
		return __instance;
	}

	/**
	 * RT���|�W�g���Ƃ��đΏۃA�h���X�ɃA�N�Z�X�\�ł��邩�ǂ����m�F����i�������j
	 * 
	 * @param address
	 *            �����Ώۂ̃A�h���X
	 * @return RT���|�W�g���Ƃ��ăA�N�Z�X�\���ǂ���
	 */
	public boolean validateNameServerAddress(String address) {
		return true;
	}

	/**
	 * URI�������Ɏ��A���|�W�g���T�[�o�̏���Ԃ�
	 * 
	 * @param address
	 *            ���|�W�g���T�[�o�̃A�h���X
	 * @return ���|�W�g���T�[�o�����̃��X�g
	 */
	public RepositoryViewItem getRepositoryServerRoot(String address) {
		if( "".equals(address) ) { //$NON-NLS-1$
			return null;
		}

		RTRepositoryClientFactory factory = RTRepositoryClientFactory.getInstance();
		RTRepositoryClient client = factory.create(address);
		String[] keywords = new String[0];
		ItemCategory itemCategory = ItemCategory.All;
		int numberOfItems = 0;
		int cautionNumber = RepositoryViewPreferenceManager.getInstance().getCaution_Count();
		try {
			numberOfItems = client.countItem(itemCategory, keywords);
		} catch (RTRepositoryAccessException e1) {
			e1.printStackTrace();
			Logger.error(null, e1);
			return null;
		}
		if( cautionNumber<=numberOfItems ) {
			if( !MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					Messages.getString("RTRepositoryAccesser.5"),Messages.getString("RTRepositoryAccesser.6") + cautionNumber + Messages.getString("RTRepositoryAccesser.7")) ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return null;
			}
		}
		
		itemCategory = ItemCategory.All;
		String[] searchResults = new String[numberOfItems];
		try {
			searchResults = client.searchItem(itemCategory, keywords);
		} catch (RTRepositoryAccessException e) {
			e.printStackTrace();
			return null;
		}
		RepositoryViewItem result = createRoot(address, searchResults);

		return result;
	}
	
	private RepositoryViewItem createRoot(String repositoryAddress, String[] source) {

		RepositoryViewRootItem result = new ServerRVRootItem(repositoryAddress);

		for(int intIdx=0;intIdx<source.length;intIdx++) {
			String[] element = source[intIdx].split(":"); //$NON-NLS-1$
			if( element.length >= 2) {
				String[] items = element[1].split("\\."); //$NON-NLS-1$
				ComponentSpecification specification  = ComponentFactory.eINSTANCE.createComponentSpecification();
				specification.setCategoryL(items[items.length-2]);
				specification.setTypeNameL(items[items.length-1]);
				specification.setAliasName(items[items.length-1]);
				specification.setComponentId(source[intIdx]);
				specification.setPathId(repositoryAddress + "/" + source[intIdx]); //$NON-NLS-1$
				
				RepositoryViewFactory.buildTree(result, specification, element[0], true);
			}
		}
		return result;
	}
	
	/**
	 * @param component		�R���|�[�l���g�d�l��ID���̑�
	 * @return				�_�E�����[�h�����R���|�[�l���g�d�l
	 * @throws Exception
	 */
	public ComponentSpecification getComponentProfile(ComponentSpecification component) throws Exception {

		RTRepositoryClientFactory factory = RTRepositoryClientFactory.getInstance();
		RTRepositoryClient client = factory.create(component.getPathId());
		ItemCategory itemCategory = ItemCategory.RTC;
		String targetXML = client.downloadProfile(component.getComponentId(), itemCategory);
		ProfileHandler handler = new ProfileHandler();
    	ComponentSpecification specification  = handler.createComponentFromXML(targetXML);
		specification.setAliasName(component.getAliasName());
		specification.setComponentId(component.getComponentId());
		specification.setPathId(component.getPathId());
		
		return specification;
	}

	/**
	 * targetServer����targetItem���폜����
	 * @param targetItem
	 * @param targetServer
	 * @throws Exception
	 */
	public void deleteProfile(String targetItem, String targetServer) throws Exception {
		ItemCategory itemCategory = getItemCategory(targetItem);
		RTRepositoryClientFactory factory = RTRepositoryClientFactory.getInstance();
		RTRepositoryClient client = factory.create(targetServer);
		client.deleteItem(targetItem, itemCategory);
	}

	/**
	 * targetServer��targetItem���A�b�v���[�h����
	 * @param targetItem
	 * @param targetServer
	 * @return
	 * @throws Exception
	 */
	public ComponentSpecification uploadProfile(String targetItem, String targetServer) throws Exception {
		String rtcXml = getTargetProfile(targetItem);
		if( rtcXml==null ) {
			throw new IOException(Messages.getString("RTRepositoryAccesser.11")); //$NON-NLS-1$
		}
		//XML�o���f�[�V����
		ProfileHandler handler = new ProfileHandler();
		ProfileHandler.validateXml(rtcXml);
		//Profile�̓o�^
		ComponentSpecification module = handler.createComponentFromXML(rtcXml);
		String itemId = module.getComponentId();
		ItemCategory itemCategory = getItemCategory(itemId);
		RTRepositoryClientFactory factory = RTRepositoryClientFactory.getInstance();
		RTRepositoryClient client = factory.create(targetServer);
		client.registerProfile(itemId, itemCategory, rtcXml, false);
		//�p�b�P�[�W�̓o�^
		itemCategory = ItemCategory.RTC;
		client.registerPackage(itemId, itemCategory, targetItem);
		
		return module;
	}

	private String getTargetProfile(String target) throws Exception {
		
		String[] targetFile = target.split("\\."); //$NON-NLS-1$
		if(targetFile[targetFile.length-1].toUpperCase().equals(TAR_EXT)) {
			return getTarProfile(target);
		} else	if(targetFile[targetFile.length-1].toUpperCase().equals(GZ_EXT)) {
				return getGzProfile(target);
		} else	if(targetFile[targetFile.length-1].toUpperCase().equals(ZIP_EXT)) {
			return getZipProfile(target);
		} else {
			return null;
		}
	}
	
	private String getGzProfile(String target) throws IOException, TarException {
		TarInputStream tarinput = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(target))));
		return readTar(tarinput);
	}

	private String getTarProfile(String target) throws IOException, TarException {
		TarInputStream tarinput = new TarInputStream(new BufferedInputStream(new FileInputStream(target)));
		return readTar(tarinput);
	}

	private String readTar(TarInputStream input) throws IOException, TarException {
			
		TarEntry tarEntry = input.getNextEntry();
		while( tarEntry!=null ){
			if( tarEntry.getName().toUpperCase().contains(RTC_XML)) {
				int size = (int)tarEntry.getSize();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(size);
				copyEntryContents(input, buffer);
				byte[] data = buffer.toByteArray(); 
				BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
				String str = new String();
				StringBuffer stbRet = new StringBuffer();
				while( (str = reader.readLine()) != null ) {
					stbRet.append(str + "\r\n"); //$NON-NLS-1$
				}
				reader.close();
				String rtcXml = stbRet.toString();
				input.close();
				return rtcXml;
			}
	        tarEntry = input.getNextEntry(); 
		}
		input.close();

        return null;
	}

	private void copyEntryContents(TarInputStream tarinput, OutputStream out) throws IOException {
        byte[] buf = new byte[32 * 1024];

        while (true) {
            int numRead = tarinput.read(buf, 0, buf.length);

            if (numRead == -1) {
                break;
            }

            out.write(buf, 0, numRead);
        }
    }
	
	private String getZipProfile(String target) throws IOException {
		
		ZipInputStream zipinput = new ZipInputStream(new BufferedInputStream(new FileInputStream(target)));
	    ZipEntry entry = null;
		while( (entry = zipinput.getNextEntry()) != null ) {
			if( !entry.isDirectory() ) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				CheckedOutputStream output = new CheckedOutputStream(
		            new BufferedOutputStream(buffer), new CRC32());
		        byte[] buf = new byte[1024];
		        int writeSize = 0;
		        int totalSize = 0;
		        while( (writeSize = zipinput.read(buf)) != -1 ) {
		        	totalSize += writeSize;
		        	output.write(buf, 0, writeSize);
		        }
		        output.close();

		        if (entry.getSize() != totalSize) {
		        	throw new IOException(Messages.getString("RTRepositoryAccesser.14")); //$NON-NLS-1$
		        }
		        if (entry.getCrc() != output.getChecksum().getValue()) {
		        	throw new IOException(Messages.getString("RTRepositoryAccesser.15")); //$NON-NLS-1$
		        }
		        if(entry.getName().toUpperCase().contains(RTC_XML)) {
		        	String rtcXml = buffer.toString();
					zipinput.closeEntry();
					zipinput.close();
					return rtcXml;
		        }
			}
			zipinput.closeEntry();
		}
		zipinput.close();

		return null;
	}
	
	private ItemCategory getItemCategory(String target) {
		String[] element = target.split(":"); //$NON-NLS-1$
		ItemCategory result = null;
		if( element.length >= 2) {
			if(element[0].equals(RepositoryViewLeafItem.RTSystem_LEAF) ) {
				result = ItemCategory.RTSystem;
			} else if(element[0].equals(RepositoryViewLeafItem.RTSenario_LEAF) ) {
				result = ItemCategory.RTScenario;
			} else if(element[0].equals(RepositoryViewLeafItem.RTModel_LEAF) ) {
				result = ItemCategory.RTModel;
			} else {
				result = ItemCategory.RTC;
			}
		}
		return result;

	}
}