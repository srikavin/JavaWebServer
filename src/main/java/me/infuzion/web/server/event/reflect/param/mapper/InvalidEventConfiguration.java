/*
 * Copyright 2020 Srikavin Ramkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.infuzion.web.server.event.reflect.param.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class InvalidEventConfiguration extends RuntimeException {
    public InvalidEventConfiguration(Annotation annotation, Method method) {
        super("Invalid event configuration for annotation " + annotation.toString() + " on method " +
                method.toGenericString() + ".");
    }

    public InvalidEventConfiguration(String message, Annotation annotation, Method method) {
        super("Invalid event configuration for annotation " + annotation.toString() + " on method " +
                method.toGenericString() + ": " + message);
    }

    public InvalidEventConfiguration(String message, Method method) {
        super("Invalid event configuration for method " + method.toGenericString() + ": " + message);
    }

    public InvalidEventConfiguration(String message, Parameter parameter, Method method) {
        super("Invalid event configuration for method " + method.toGenericString() + ": " + message);
    }
}
