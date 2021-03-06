/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.server.asciidoctorj;
import static org.asciidoctor.Asciidoctor.Factory.*;
import static org.asciidoctor.OptionsBuilder.*;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;

import de.jcup.asp.api.Backend;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;
import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;

/**
 * Just a playground to use asciidoctorj directly and to check the outputs
 * @author albert
 *
 */
public class AsciidocTestMain {

    public static void main(String[] args) throws Exception {
        System.out.println("Creating asciidoctor instance");
        Asciidoctor asciidoctor = AsciidoctorService.INSTANCE.getAsciidoctor();
        System.out.println("Creating tmp file");
        Path adocfile = Files.createTempFile("test", ".adoc");
        Files.newBufferedWriter(adocfile).append("== Interests in asciidoc\nsomething important...\n\nTIP: Do not write word, but asciidoc ;-)").close();
        File xadocfile =adocfile.toFile();
                
        System.out.println("Convert tmp file");
        // see https://github.com/asciidoctor/asciidoctorj/blob/master/docs/integrator-guide.adoc
        asciidoctor.convertFile(adocfile.toFile(), options().attributes(
                AttributesBuilder.attributes()        
                .icons(Attributes.FONT_ICONS) 
                .attribute("foo", "bar")      // (3)
                .get()).backend(Backend.PDF.convertToString()).safe(SafeMode.UNSAFE).get());
        
        TargetFileNameProvider provider = new TargetFileNameProvider();
        
        File resolveTargetFileFor = provider.resolveTargetFileFor(xadocfile,Backend.PDF);
        System.out.println("Open converted file:"+resolveTargetFileFor);
        Desktop.getDesktop().open(resolveTargetFileFor);
        System.out.println("opened PDF in external view");
        System.exit(0);
    }
}
