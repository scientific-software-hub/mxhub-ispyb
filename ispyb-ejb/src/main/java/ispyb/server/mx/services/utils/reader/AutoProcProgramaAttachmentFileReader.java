package ispyb.server.mx.services.utils.reader;

import ispyb.common.util.PathUtils;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoProcProgramaAttachmentFileReader {
	
	
	@SuppressWarnings("unchecked")
	public static List<AutoProcessingData> getAutoProcessingDataFromAttachemt(AutoProcProgramAttachment3VO attachment) throws Exception {
		return (List<AutoProcessingData>) AutoProcProgramaAttachmentFileReader.readAttachment(attachment).get("autoProcessingData");
	}
	
	
	public static HashMap<String, Object> readAttachment(AutoProcProgramAttachment3VO attachment) throws Exception {
		boolean xscaleFile = false;
		boolean truncateLog = false;
		boolean noanomAimlessLog = false;
		ArrayList<Double> fastdp_cc2 = new ArrayList<Double>();
		ArrayList<Double> fastdp_completeness = new ArrayList<Double>();
		ArrayList<Double> fastdp_rfactor = new ArrayList<Double>();
		ArrayList<Double> fastdp_isigma = new ArrayList<Double>();
		ArrayList<Double> fastdp_dmid = new ArrayList<Double>();
		List<AutoProcessingData> listAutoProcessingData = new ArrayList<AutoProcessingData>();
		
//		System.out.println(attachment);
		if (attachment != null) {
			String fileName = attachment.getFileName();
			xscaleFile = (fileName != null && fileName.toLowerCase().endsWith("xscale.lp"));
			truncateLog = (fileName != null && fileName.toLowerCase().endsWith(".log") && fileName.toLowerCase().contains("truncate"));
			noanomAimlessLog = (fileName != null && fileName.toLowerCase().endsWith(".log") && fileName.toLowerCase().contains("aimless"));
			boolean autoProcLog = (fileName != null && fileName.toLowerCase().endsWith(".log") && fileName.toLowerCase().contains("autoproc"));

//			System.out.println(xscaleFile);
//			System.out.println(truncateLog);
			if (xscaleFile || truncateLog || noanomAimlessLog || autoProcLog) {
				// parse the file
				String sourceFileName = PathUtils.FitPathToOS(attachment.getFilePath() + "/" + fileName);
				BufferedReader inFile = null;
				String output = new String();// = null;

				try {
					// 1. Reading input by lines:
					boolean startToRead = false;
					boolean startToRead2 = false;
					boolean startToReadFastDPCC2 = false;
					boolean startToReadFastDPCompleteness = false;
					boolean startToReadFastDPRfactor = false;
					boolean startToReadFastDPIsigma = false;
					boolean endToReadFastDPCC2 = false;
					boolean endToReadFastDPCompleteness = false;
					boolean endToReadFastDPRfactor = false;
					boolean endToReadFastDPIsigma = false;
					boolean endToRead = false;
					boolean endToReadFastDP = false;
					System.out.println(sourceFileName);
					inFile = new BufferedReader(new FileReader(sourceFileName));
					String s = new String();
					while ((s = inFile.readLine()) != null) {
						String line = s;
//						System.out.println(line);
						output += line + "\n";
						if (xscaleFile) {
							if (line.contains("SUBSET OF INTENSITY DATA WITH SIGNAL/NOISE")) {
								startToRead = true;
							} else if (startToRead) {
								CorrectLpRow row = CorrectLpRow.parse(line);
								if (row != null) {
									listAutoProcessingData.add(new AutoProcessingData(attachment.getAutoProcProgramAttachmentId().toString(),
											row.resolutionLimit, row.completeness, row.rFactorObserved,
											row.iSigma, row.cc2, row.sigAno, row.anomalCorr,
											fileName, attachment.getAutoProcProgramVOId()));
								}
							}
							if (line.contains("STATISTICS OF INPUT DATA SET")) {
								startToRead = false;
							}
						} else if (autoProcLog) {
							// autoPROC embeds two CORRECT.LP-format tables (post-refinement and
							// final processing) with an identical header line.  Reset the list each
							// time the header is seen so the LAST table (= final processing) wins.
							if (line.contains("RESOLUTION") && line.contains("NUMBER OF REFLECTIONS")
									&& line.contains("COMPLETENESS")) {
								startToRead = true;
								listAutoProcessingData.clear();
							}
							if (startToRead) {
								CorrectLpRow row = CorrectLpRow.parse(line);
								if (row != null) {
									listAutoProcessingData.add(new AutoProcessingData(attachment.getAutoProcProgramAttachmentId().toString(),
											row.resolutionLimit, row.completeness, row.rFactorObserved,
											row.iSigma, row.cc2, row.sigAno, row.anomalCorr,
											fileName, attachment.getAutoProcProgramVOId()));
								}
							}
						} else if (truncateLog) {
							if (line.contains("$TABLE: Wilson Plot")) {
								startToRead = true;
							} else if (startToRead) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[10];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									AutoProcessingData d = new AutoProcessingData(
//											attachment.getAutoProcProgramAttachmentId(),
											attachment.getFileName(),
											Double.parseDouble(val[5]), Double.parseDouble(val[7]), fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							} else if (line.contains("$TABLE: Cumulative intensity distribution")) {
								startToRead2 = true;
							} else if (startToRead2) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[6];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									// String autoProcProgramAttachmentId, Double resolutionLimit, Double completeness,
									//			Double rFactorObserved, Double iSigma, Double cc2, Double sigAno, Integer anomalCorr, String fileName, Integer autoProcProgramId
//									AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
									AutoProcessingData d = new AutoProcessingData(attachment.getFileName(),
											Double.parseDouble(val[0]), Double.parseDouble(val[1]), Double.parseDouble(val[2]),
											Double.parseDouble(val[3]), Double.parseDouble(val[4]), Double.parseDouble(val[5]),
											fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							}
							if (line.contains("<hr>")) {
								startToRead = false;
							}

						} else if (noanomAimlessLog) {
							if (line.contains("Analysis against resolution, XDSdataset")){
								startToReadFastDPRfactor = true;
								startToReadFastDPCompleteness = false;
								startToReadFastDPIsigma = true;
								startToReadFastDPCC2 = false;
								endToRead = false;
							} else if (line.contains("Completeness & multiplicity v. resolution, XDSdataset")) {
								startToReadFastDPRfactor = false;
								startToReadFastDPCompleteness = true;
								startToReadFastDPIsigma = false;
								startToReadFastDPCC2 = false;
								endToRead = false;
							} else if (line.contains("Correlations CC(1/2) within dataset, XDSdataset")) {
								startToReadFastDPRfactor = false;
								startToReadFastDPCompleteness = false;
								startToReadFastDPIsigma = false;
								startToReadFastDPCC2 = true;
								endToRead = false;
							}
							startToRead = startToReadFastDPRfactor || startToReadFastDPCompleteness || startToReadFastDPIsigma || startToReadFastDPCC2;
							if (startToRead && !endToRead) {
								if (!line.contains("$$") && !line.isEmpty() && !line.contains("I/sigma") && !line.contains("I/sigma")
										&& !line.contains("Filtered") && !line.contains("Mean") && !line.contains("Rmerge")
										&& !line.contains("Average") && !line.contains("Fractional")
										&& !line.contains("$GRAPHS:Completeness v Resolution")
										&& !line.contains(":Multiplicity v Resolution")
										&& !line.contains("$GRAPHS: CC(1/2) v resolution")
										&& !line.contains("RMS anomalous correlation ratio")
										&& !line.contains("Analysis against resolution, XDSdataset")
										&& !line.contains("Completeness & multiplicity v. resolution, XDSdataset")
										&& !line.contains("$TABLE:  Correlations CC(1/2) within dataset, XDSdataset:")
										&& !line.contains("Rsplit")
										&& !line.contains("Overall")) {
									String[] values = line.split(" ");
									String[] val = new String[20];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 19 && !values[k].isEmpty()) {
											//val[i] = values[k];
											if ("-".equals(values[k])) {
												val[i] = "0";
											} else {
												val[i] = values[k];
											}
											i++;
										}
									}

									if (startToReadFastDPRfactor || startToReadFastDPIsigma){
										fastdp_dmid.add(Double.parseDouble(val[2]));
										fastdp_rfactor.add(100 * Double.parseDouble(val[6]));
										fastdp_isigma.add(Double.parseDouble(val[13]));
									} else if (startToReadFastDPCC2){
										fastdp_cc2.add(100 * Double.parseDouble(val[6]));
									} else if (startToReadFastDPCompleteness){
										fastdp_completeness.add(Double.parseDouble(val[6]));
									}
								}
							}
							if (startToRead && line.contains("Overall")) {
								if (startToReadFastDPRfactor || startToReadFastDPIsigma){
									endToReadFastDPRfactor = true;
									endToReadFastDPIsigma = true;
								} else if (startToReadFastDPCC2){
									endToReadFastDPCC2 = true;
								} else if (startToReadFastDPCompleteness){
									endToReadFastDPCompleteness = true;
								}
								endToRead = true;
								startToReadFastDPRfactor = false;
								startToReadFastDPCompleteness = false;
								startToReadFastDPIsigma = false;
								startToReadFastDPCC2 = false;
								endToReadFastDP = endToReadFastDPRfactor && endToReadFastDPCompleteness && endToReadFastDPIsigma && endToReadFastDPCC2;

							}
						}
					}
					inFile.close();

					if (noanomAimlessLog) {
						try {
							int imax = 21;
							for (int i = 0; i < imax; i++) {
								AutoProcessingData d = new AutoProcessingData(attachment.getFileName(),
										fastdp_dmid.get(i), fastdp_completeness.get(i), fastdp_rfactor.get(i),
										fastdp_isigma.get(i), fastdp_cc2.get(i), null,
										null, fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
								listAutoProcessingData.add(d);
							}

						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}

				} catch (Exception e) {
					throw e;
				} finally {
					if (inFile != null) {
						try {
							inFile.close();
						} catch (IOException ioex) {
							// ignore
							output = "nofile";
						}
					}

				}
			}
		}
		// o[0] is a boolean xscaleFile
		// o[1] is a boolean truncateLog
		// o[2] is List<AutoProcessingData>
		// o[2] is mergedNoanomCorrectFile
//		Object[] o = new Object[3];
//		o[0] = xscaleFile;
//		o[1] = truncateLog;
//		o[2] = listAutoProcessingData;
//		o[3] = mergedNoanomCorrectFile
		
		HashMap<String, Object> o = new HashMap<String, Object>();
		o.put("xscaleFile", xscaleFile);
		o.put("truncateLog", truncateLog);
		o.put("autoProcessingData", listAutoProcessingData);
		o.put("noanomAimlessLog", noanomAimlessLog);
		return o;
	}

	/**
	 * Parses one data row from a CORRECT.LP / XDS CORRECT statistics table.
	 * Both standalone XSCALE.LP files and the autoPROC.log embedded table use
	 * this identical column layout:
	 *
	 * [0]  resolutionLimit   [1] nObs   [2] nUnique  [3] nPossible
	 * [4]  completeness%     [5] rFactorObserved%     [6] rFactorExpected%
	 * [7]  nCompared         [8] iSigma [9] rMeas%
	 * [10] cc2%              [11] anomalCorr (integer, may be negative)
	 * [12] sigAno            [13] nAno
	 */
	static class CorrectLpRow {
		final double resolutionLimit;
		final double completeness;
		final double rFactorObserved;
		final double iSigma;
		final double cc2;
		final double sigAno;
		final int anomalCorr;

		private CorrectLpRow(double resolutionLimit, double completeness, double rFactorObserved,
				double iSigma, double cc2, double sigAno, int anomalCorr) {
			this.resolutionLimit = resolutionLimit;
			this.completeness    = completeness;
			this.rFactorObserved = rFactorObserved;
			this.iSigma          = iSigma;
			this.cc2             = cc2;
			this.sigAno          = sigAno;
			this.anomalCorr      = anomalCorr;
		}

		/** Returns null for header lines, the "total" row, or any non-data text. */
		static CorrectLpRow parse(String line) {
			String[] t = line.trim().split("\\s+");
			if (t.length < 13) return null;
			for (int k = 0; k < t.length; k++) {
				if (t[k].endsWith("%") || t[k].endsWith("*"))
					t[k] = t[k].substring(0, t[k].length() - 1);
			}
			try {
				return new CorrectLpRow(
						Double.parseDouble(t[0]),   // resolutionLimit
						Double.parseDouble(t[4]),   // completeness
						Double.parseDouble(t[5]),   // rFactorObserved
						Double.parseDouble(t[8]),   // iSigma
						Double.parseDouble(t[10]),  // cc2
						Double.parseDouble(t[12]),  // sigAno
						Integer.parseInt(t[11])     // anomalCorr
				);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
}
