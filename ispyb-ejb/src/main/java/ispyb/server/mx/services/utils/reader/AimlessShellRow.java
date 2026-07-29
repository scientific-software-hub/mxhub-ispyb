package ispyb.server.mx.services.utils.reader;

import java.util.Optional;

/**
 * One data row from an AIMLESS/STARANISO "detailed statistics" per-shell
 * table, e.g.:
 * {@code 55.964 - 3.551  1641 15428  0.032 0.034 0.011  1641  62.785  1.0000 1.0000  9.40 5.52  0.9987  1138 -0.2235  0.952}
 *
 * <p>Column layout (0-based, whitespace-split): [0] low-res [1]="-"
 * [2] high-res [3] nObs [4] nUnique [5] Rmerge [6] Rmeas [7] Rpim [8] nCompared
 * [9] I/sigI [10] Compl.(all) [11] Compl.(spherical) [12] Mult.(all)
 * [13] Mult.(spherical) [14] CC(1/2) [15] Anom.Compl. [16] CC(ano) [17] SigAno
 * (STARANISO tables carry two extra trailing "Compl. Ellip." columns, ignored).
 */
record AimlessShellRow(double resolutionLimit, double completeness, double rFactorObserved,
		double iSigma, double cc2, double sigAno, int anomalCorr) {

	static Optional<AimlessShellRow> parse(String line) {
		String[] t = line.trim().split("\\s+");
		if (t.length < 18 || !"-".equals(t[1])) {
			return Optional.empty();
		}
		try {
			return Optional.of(new AimlessShellRow(
					Double.parseDouble(t[2]),
					100 * Double.parseDouble(t[10]),
					100 * Double.parseDouble(t[5]),
					Double.parseDouble(t[9]),
					100 * Double.parseDouble(t[14]),
					Double.parseDouble(t[17]),
					Math.round(100 * Float.parseFloat(t[16]))));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
}
