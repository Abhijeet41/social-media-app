package com.abhi41.socialmediaapp.untils;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

public class Zipper {
    private String password;
    private static final String EXTENSION = "zip";


    public Zipper(String password)
    {
        this.password = password;
    }

    public void pack(String filePath, File dir) throws ZipException
    {
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
// Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

            String baseFileName = FilenameUtils.getBaseName(filePath);
            String destinationFile = dir+"//"+baseFileName + "." + EXTENSION;
            ZipFile zipFile = new ZipFile(destinationFile,password.toCharArray());
            zipFile.addFile(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unpack(String sourceZipFilePath, String extractedZipFilePath) throws ZipException
    {
        try {
            ZipFile zipFile = new ZipFile(sourceZipFilePath + "." + EXTENSION);

            if (zipFile.isEncrypted())
            {
                zipFile.setPassword(password.toCharArray());
            }

            zipFile.extractAll(extractedZipFilePath);
        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }
    }

}
