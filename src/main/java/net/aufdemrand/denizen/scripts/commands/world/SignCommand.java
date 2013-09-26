package net.aufdemrand.denizen.scripts.commands.world;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dList;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.Utilities;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

/**
 * Creates a sign with a certain text at a location.
 *
 * @author David Cernat
 */

public class SignCommand extends AbstractCommand {

    private enum Type { SIGN_POST, WALL_SIGN }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values()))

                scriptEntry.addObject("type", Type.valueOf(arg.getValue().toUpperCase()));

            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(dLocation.class))

                scriptEntry.addObject("location", arg.asType(dLocation.class).setPrefix("location"));

            else if (!scriptEntry.hasObject("text")
                    && arg.matchesArgumentType(dList.class))

                scriptEntry.addObject("text", arg.asType(dList.class));

            else if (!scriptEntry.hasObject("direction")
                    && arg.matchesPrefix("direction, dir"))
                scriptEntry.addObject("direction", arg.asElement());

            else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg.raw_value);
        }

        // Check to make sure required arguments have been filled

        if (!scriptEntry.hasObject("location"))
            throw new InvalidArgumentsException("Must specify a Sign location!");

        // Default to SIGN_POST type
        scriptEntry.defaultObject("type", Type.SIGN_POST);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(final ScriptEntry scriptEntry) throws CommandExecutionException {

        // Get objects
        String direction = scriptEntry.hasObject("direction") ? ((Element) scriptEntry.getObject("direction")).asString() : null;
        Type type = (Type) scriptEntry.getObject("type");
        dList text = (dList) scriptEntry.getObject("text");
        dLocation location = (dLocation) scriptEntry.getObject("location");

        // Report to dB
        dB.report(getName(), type.name() + ", "
                + aH.debugObj("location", location)
                + aH.debugObj("text", text));

        Block sign = location.getBlock();
        sign.setType(Material.valueOf(type.name()));
        BlockState signState = sign.getState();

        Utilities.setSignLines((Sign) signState, text.toArray());
        if (direction != null)
            Utilities.setSignRotation(signState, direction);
        else
            Utilities.setSignRotation(signState);
    }
}
