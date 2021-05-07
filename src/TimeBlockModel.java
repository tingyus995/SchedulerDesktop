import java.sql.Time;
import java.util.ArrayList;

public class TimeBlockModel {

    private static final String rootDir = "db/blocks";

    public static TimeBlock[] getAll(){
        ArrayList<TimeBlock> blocks = TimeBlock.getAll(rootDir);
        return blocks.toArray(new TimeBlock[0]);
    }

    public static TimeBlock[] getWeekTimeBlocks(Week week){
        ArrayList<TimeBlock> result = new ArrayList<>();
        TimeBlock[] allBlocks = getAll();
        for(TimeBlock block: allBlocks){
            if(week.isInWeek(block.getDate())){
                result.add(block);
            }
        }
        return result.toArray(new TimeBlock[0]);
    }

}
