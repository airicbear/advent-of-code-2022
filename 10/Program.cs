using _10.Properties;

string[] testInput = Resources.test_input.Split('\n');
string[] puzzleInput = Resources.puzzle_input.Split('\n');
CPU cpu = new();
cpu.Run(testInput);
cpu.Run(puzzleInput);

class CPU
{
    private int X { get; set; } = 1;
    private int cycle { get; set; } = 0;
    private int signalStrength { get; set; } = 0;

    private int Signal()
    {
        return X * cycle;
    }

    private void Update()
    {
        cycle++;

        if ((cycle - 20) % 40 == 0)
        {
            Console.WriteLine(cycle);
            signalStrength += Signal();
        }
    }

    private void Reset()
    {
        X = 1;
        cycle = 0;
        signalStrength = 0;
    }

    public void Run(string[] input)
    {
        Reset();
        for (int i = 0; i < input.Length; i++)
        {
            string[] tokens = input[i].Split(' ');

            if (tokens[0].Trim().Equals("noop"))
            {
                Update();
            }
            else if (tokens[0].Trim().Equals("addx"))
            {
                Update();
                Update();
                X += int.Parse(tokens[1]);
            }
        }

        Console.WriteLine(signalStrength);
    }
}