import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class PersistantStorage {
}

class Schema implements Serializable {

    String rootDir;
    String filename;

    void setFileName(String filename){
        this.filename = filename;
    }

    String getFilePath(){
        return Paths.get(rootDir, filename).toString();
    }



    void save(){
        if(filename == null){
            UUID id = UUID.randomUUID();
            filename = id.toString();
        }
        Path fullPath = Paths.get(rootDir, filename);

        try {

            FileOutputStream fileOut =
                    new FileOutputStream(fullPath.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    void delete(){
        if(filename != null){
            Path fullPath = Paths.get(rootDir, filename);

            try {
                Files.delete(fullPath);

            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    public String getID(){
        return filename;
    }

    void remove(){
        if(filename == null) return;
        try {
            Files.delete((new File(filename)).toPath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings(value = "unchecked")
    static <T>  T load(String path){
        T schema = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            schema = (T) in.readObject();
            in.close();
            fileIn.close();
            return schema;
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
            return null;
        }
    }

    static <T> ArrayList<T> getAll(String rootDir){
        ArrayList<T> result = new ArrayList<T>();
        try {
            Files.list(new File(rootDir).toPath()).forEach(path -> {
                if(!path.getFileName().toString().startsWith(".")) {
                    T t = Schema.load(path.toString());
                    if (t != null) {
                        result.add(t);
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
