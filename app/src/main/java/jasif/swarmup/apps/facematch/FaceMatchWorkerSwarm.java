package jasif.swarmup.apps.facematch;

import android.content.Context;
import android.util.Log;
import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.FileFactory;
import jasif.swarmup.common.JobParams;
import jasif.swarmup.worker.WorkerSwarm;

public class FaceMatchWorkerSwarm extends WorkerSwarm {
	private SearchImage imageSearch = new SearchImage();
	public FaceMatchWorkerSwarm(Context pAct, String pActivityClass,
                                JobParams pMsg, int pIndex, boolean stolen) {
		super(pAct, pActivityClass, pMsg, pIndex, stolen);
	}
	@Override
	public CompletedJob doAppSpecificJob(Object pParam) {
		if (pParam != null) {
			if (pParam instanceof String) {
				CompletedJob cj = null;
				String pS = (String) pParam;
				
				String extension = FileFactory.getInstance().getFileExtension(pS);
				if ((extension.equalsIgnoreCase(FaceConstants.FILE_EXTENSION_JPEG))
						|| (extension
								.equalsIgnoreCase(FaceConstants.FILE_EXTENSION_JPG))) {
					Integer res = Integer.valueOf(imageSearch.search(pS));
					cj = new CompletedJob(
							CommonConstants.READ_STRING_MODE, FileFactory.getInstance()
									.getFileNameFromFullPath(pS), -1, null);
					cj.intValue = res.intValue();
					FaceResult.getInstance().addToMap(cj.stringValue, cj.intValue);
					
					
					return cj;
				} else {
					Log.d("EXTENSION OTHER = ", extension);
				}

			}
		}
		return null;
	}

}
