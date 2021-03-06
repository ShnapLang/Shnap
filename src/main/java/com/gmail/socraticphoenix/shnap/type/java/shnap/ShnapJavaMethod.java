/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gmail.socraticphoenix.shnap.type.java.shnap;

import com.gmail.socraticphoenix.shnap.run.env.ShnapEnvironment;
import com.gmail.socraticphoenix.shnap.type.java.MultiJavaMethod;
import com.gmail.socraticphoenix.shnap.util.ShnapFactory;
import com.gmail.socraticphoenix.shnap.type.object.ShnapFunction;
import com.gmail.socraticphoenix.shnap.parse.ShnapLoc;
import com.gmail.socraticphoenix.shnap.type.object.ShnapObject;
import com.gmail.socraticphoenix.shnap.program.context.ShnapExecution;
import com.gmail.socraticphoenix.shnap.type.natives.ShnapJavaBackedNative;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShnapJavaMethod extends ShnapFunction {
    private ShnapJavaBackedNative owner;
    private MultiJavaMethod method;

    public ShnapJavaMethod(ShnapLoc loc, MultiJavaMethod method, ShnapJavaBackedNative owner) {
        super(loc, new ArrayList<>(), ShnapFactory.instSimple(ShnapObject::getVoid));
        this.method = method;
        this.owner = owner;
    }

    @Override
    public ShnapExecution invokeOperator(List<ShnapObject> values, int order, ShnapEnvironment tracer) {
        if (order == 2) {
            tracer.popTraceback();
            return ShnapExecution.normal(ShnapObject.getVoid(), tracer, this.getLocation());
        }

        ShnapExecution e = this.method.execute(owner, values, tracer);

        if(e.getState() == ShnapExecution.State.THROWING) {
            return e;
        } else if (e.getState() == ShnapExecution.State.RETURNING) {
            tracer.popTraceback();
            return ShnapExecution.normal(e.getValue(), tracer, this.getLocation());
        } else {
            tracer.popTraceback();
            return ShnapExecution.normal(ShnapObject.getVoid(), tracer, this.getLocation());
        }
    }

    @Override
    protected ShnapExecution invokePrivate(List<ShnapObject> values, Map<String, ShnapObject> defValues, ShnapEnvironment tracer) {
        ShnapExecution e = this.method.execute(owner, values, tracer);

        if(e.getState() == ShnapExecution.State.THROWING) {
            return e;
        } else if (e.getState() == ShnapExecution.State.RETURNING) {
            tracer.popTraceback();
            return ShnapExecution.normal(e.getValue(), tracer, this.getLocation());
        } else {
            tracer.popTraceback();
            return ShnapExecution.normal(ShnapObject.getVoid(), tracer, this.getLocation());
        }
    }

}
