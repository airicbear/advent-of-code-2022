using _10.Properties;

string[] testInput = Resources.test_input.Split('\n');
string[] puzzleInput = Resources.puzzle_input.Split('\n');
CPU cpu = new();
cpu.Run(testInput);
cpu.Run(puzzleInput);
CRT crt = new();
crt.Run(testInput);
crt.Run(puzzleInput);

class CPU
{
    private int X = 1;
    private int cycle = 0;
    private int signalStrength = 0;

    private int Signal()
    {
        return X * cycle;
    }

    private void UpdateSignal()
    {
        if ((cycle - 20) % 40 == 0)
        {
            signalStrength += Signal();
        }
    }

    private void Update()
    {
        cycle++;
        UpdateSignal();
    }

    private void Reset()
    {
        X = 1;
        cycle = 0;
        signalStrength = 0;
    }

    private void Noop()
    {
        Update();
    }

    private void Addx(int V)
    {
        Update();
        Update();
        X += V;
    }

    public void Run(string[] input)
    {
        Reset();
        for (int i = 0; i < input.Length; i++)
        {
            string line = input[i];
            string[] tokens = line.Split(' ');
            string instruction = tokens[0].Trim();

            if (instruction.Equals("noop"))
            {
                Noop();
            }
            else if (instruction.Equals("addx"))
            {
                int V = int.Parse(tokens[1]);

                Addx(V);
            }
        }

        Console.WriteLine(signalStrength);
    }
}


class CRT
{
    private int X = 1;
    private int cycle = 0;
    private const int screenWidth = 40;
    private const char pixelLit = '#';
    private const char pixelDark = '.';

    private bool ShouldDraw()
    {
        return Math.Abs(cycle % 40 - X) <= 1;
    }

    private void Update()
    {
        if (ShouldDraw())
        {
            Console.Write(pixelLit);
        }
        else
        {
            Console.Write(pixelDark);
        }

        cycle++;

        if (cycle > 0 && cycle % screenWidth == 0)
        {
            Console.WriteLine();
        }
    }

    private void Reset()
    {
        X = 1;
        cycle = 0;
    }

    private void Noop()
    {
        Update();
    }

    private void Addx(int V)
    {
        Update();
        Update();
        X += V;
    }

    public void Run(string[] input)
    {
        Reset();
        for (int i = 0; i < input.Length; i++)
        {
            string line = input[i];
            string[] tokens = line.Split(' ');
            string instruction = tokens[0].Trim();

            if (instruction.Equals("noop"))
            {
                Noop();
            }
            else if (instruction.Equals("addx"))
            {
                int V = int.Parse(tokens[1]);

                Addx(V);
            }
        }
    }
}