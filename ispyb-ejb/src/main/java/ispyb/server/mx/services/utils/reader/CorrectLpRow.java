package ispyb.server.mx.services.utils.reader;

/**
 * Parses one data row from a CORRECT.LP / XDS CORRECT statistics table.
 * Both standalone XSCALE.LP files (also produced by EDNA_proc and XDSAPP)
 * use this identical column layout:
 *
 * [0]  resolutionLimit   [1] nObs   [2] nUnique  [3] nPossible
 * [4]  completeness%     [5] rFactorObserved%     [6] rFactorExpected%
 * [7]  nCompared         [8] iSigma [9] rMeas%
 * [10] cc2%              [11] anomalCorr (integer, may be negative)
 * [12] sigAno            [13] nAno
 */
class CorrectLpRow {
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
