/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package org.uli.wsdldiff;

import java.util.List;
import com.predic8.wsdl.*;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import com.predic8.soamodel.Difference;

public class Main {
    static private final String NAME="wsdldiff";

    static public void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static public int run(String[] args) {
	String wsdl1 = args[0];
	String wsdl2 = args[1];

	WSDLParser parser = new WSDLParser();

	Definitions wsdl1d = parser.parse(wsdl1);
	Definitions wsdl2d = parser.parse(wsdl2);

	WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1d, wsdl2d);
	List<Difference> lst = diffGen.compare();
	for (Difference diff : lst) {
	    dumpDiff(diff, "");
	}
	int exitCode = lst.size() > 0 ? 1 : 0;
	if (exitCode != 0) {
	    System.err.println("WSDLs are different!");
	}
	return exitCode;
    }
    private static void dumpDiff(Difference diff, String level) {
	System.out.println(level + diff.getDescription());
	for (Difference localDiff : diff.getDiffs()) {
	    dumpDiff(localDiff, level + "  ");
	}
    }
}

