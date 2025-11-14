import { cn } from "../../lib/utils";

interface SpinnerProps {
  className?: string;
  label?: string;
}

export function Spinner({ className, label }: SpinnerProps) {
  return (
    <div className={cn("flex items-center gap-3 text-muted-foreground", className)}>
      <span className="inline-block h-4 w-4 animate-spin rounded-full border-2 border-primary border-r-transparent" />
      {label ? <span className="text-sm">{label}</span> : null}
    </div>
  );
}

