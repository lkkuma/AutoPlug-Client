/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.client.network.local;

import com.osiris.autoplug.client.configs.GeneralConfig;
import com.osiris.autoplug.core.logger.AL;
import com.osiris.dyml.exceptions.DYReaderException;
import com.osiris.dyml.exceptions.DuplicateKeyException;
import com.osiris.dyml.exceptions.IllegalListException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Deprecated
public class LocalConnectionValidator {


    LocalConnectionValidator(Socket local_socket, @NotNull DataInputStream local_dis, @NotNull DataOutputStream local_dos) {

        Thread newThread = new Thread(() -> {

            try {

                AL.info("Validating current AutoPlugPlugin connection...");

                boolean matches = local_dis.readUTF().equals(new GeneralConfig().server_key.asString());

                if (matches) {
                    local_dos.writeUTF("true");
                    AL.info("Keys match!");

                    new LocalTaskReceivePlugins(local_socket, local_dis, local_dos);
                } else {
                    local_dos.writeUTF("false");
                    AL.info("Wrong AutoPlugPlugin! Validation failed!");
                }

            } catch (@NotNull IOException | DuplicateKeyException | DYReaderException | IllegalListException e) {
                e.printStackTrace();
            }

        });
        newThread.start();
    }


}
